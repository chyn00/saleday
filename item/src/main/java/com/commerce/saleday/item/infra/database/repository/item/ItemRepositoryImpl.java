package com.commerce.saleday.item.infra.database.repository.item;

import static com.commerce.saleday.item.domain.item.model.QItem.item;
import static com.commerce.saleday.item.domain.review.model.QReview.review;

import com.commerce.saleday.item.domain.item.model.Item;
import com.commerce.saleday.item.domain.item.repository.ItemRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

//Jpa Data Repository를 활용하여, 인프라스트럭쳐 레이어에서의 JPA 종속적 로직(도메인에서 종속성을 떼어내기 위해 이렇게 사용)
//필요하면 쿼리 DSL도 이 구현체에 만들어 놓는다.
@Slf4j
@Repository
public class ItemRepositoryImpl implements ItemRepository {

  public static final int BATCH_SIZE = 1000;
  private final EntityManager entityManager;
  private final ItemJpaRepository itemJpaRepository;
  private final JPAQueryFactory jpaQueryFactory;

  public ItemRepositoryImpl(EntityManager entityManager, ItemJpaRepository itemJpaRepository,
      JPAQueryFactory jpaQueryFactory) {
    this.entityManager = entityManager;
    this.itemJpaRepository = itemJpaRepository;
    this.jpaQueryFactory = jpaQueryFactory;
  }

  @Override
  public Optional<Item> findItemByCode(String itemCode) {
    return itemJpaRepository.findByCode(itemCode);
  }

  @Override
  public List<Item> findItemsByCode(List<String> itemCodeList) {
    return itemJpaRepository.findByCodeIn(itemCodeList);
  }

  @Override
  public Item save(Item item) {
    return itemJpaRepository.save(item);
  }

  //saveAll은 벌크 저장 시 review의 값을 모두, select 하는 문제가 있어서, persistAll로 변경
  @Override
  public void persistAll(List<Item> items) {
    for (int i = 0; i < items.size(); i++) {
      entityManager.persist(items.get(i));
      //처음 0은 제외하고 999일때마다 flush
      if ((i+1) % BATCH_SIZE == 0) {
        entityManager.flush();
        entityManager.clear();
      }
    }

    // 남은 나머지 처리
    if (items.size() % BATCH_SIZE != 0) {
      entityManager.flush();
      entityManager.clear();
    }
  }

  @Override
  public Page<Item> findByCodeContains(String code, Pageable pageable) {

    // 콘텐츠 조회
    // Item만 페이징 (리뷰는 join하지 않음)
    // 페이징 처리 할때는 fetch join 쓰지 않는것 권장. offset limit이 join된 결과를 return하여 문제 일어남
    List<Item> items = jpaQueryFactory
        .selectFrom(item)
        .where(item.code.contains(code))
        .orderBy(item.id.asc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    // Step 2: Item ID 목록 추출
    List<Long> itemIds = items.stream()
        .map(Item::getId)
        .toList();

    if (itemIds.isEmpty()) {
      return new PageImpl<>(List.of(), pageable, 0);
    }

    // Step 3: ID 기반으로 리뷰 join fetch
    List<Item> content = jpaQueryFactory
        .selectFrom(item)
        .leftJoin(item.reviews, review).fetchJoin()
        .where(item.id.in(itemIds))
        .orderBy(item.id.asc())
        .fetch();

    //todo: count 총 계산은 비효율적(CUD 할 때만 item 총 개수의 캐싱이 변경되도록 하면됨.)
    //content.size()는 현재 조회값의 개수이기 때문에, 대규모 트래픽에서는 total size는 캐싱으로 따로분리 필요
    return new PageImpl<>(content, pageable, content.size());//<>는 제네릭 타입 문법, new HashMap<>()과 같음
  }
}

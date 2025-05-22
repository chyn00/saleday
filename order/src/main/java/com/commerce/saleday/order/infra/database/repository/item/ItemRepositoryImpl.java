package com.commerce.saleday.order.infra.database.repository.item;

import static com.commerce.saleday.order.domain.item.model.QItem.item;

import com.commerce.saleday.order.domain.item.model.Item;
import com.commerce.saleday.order.domain.item.repository.ItemRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

//Jpa Data Repository를 활용하여, 인프라스트럭쳐 레이어에서의 JPA 종속적 로직(도메인에서 종속성을 떼어내기 위해 이렇게 사용)
//필요하면 쿼리 DSL도 이 구현체에 만들어 놓는다.
@Repository
public class ItemRepositoryImpl implements ItemRepository {

  private final ItemJpaRepository itemJpaRepository;
  private final JPAQueryFactory jpaQueryFactory;

  public ItemRepositoryImpl(ItemJpaRepository itemJpaRepository,
      @Qualifier("orderQueryFactory") JPAQueryFactory jpaQueryFactory) {
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

  @Override
  public Page<Item> findByCodeContains(String code, Pageable pageable) {
    // 콘텐츠 조회
    List<Item> content = jpaQueryFactory
        .selectFrom(item)
        .join(item.reviews).fetchJoin()
        .where(item.code.contains(code))
        .orderBy(item.id.asc())//추후 유동적으로 변경가능하나 페이지의 일관성을 보장하기 위해, 인덱스 정렬 기준을 부여
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    //todo: count 총 계산은 비효율적(CUD 할 때만 item 총 개수의 캐싱이 변경되도록 하면됨.)
    //content.size()는 현재 조회값의 개수이기 때문에, 대규모 트래픽에서는 total size는 캐싱으로 따로분리 필요
    return new PageImpl<>(content, pageable, content.size());//<>는 제네릭 타입 문법, new HashMap<>()과 같음
  }
}

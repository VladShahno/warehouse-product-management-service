package warehouse.com.productmanagementservice.service.schedular;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import warehouse.com.productmanagementservice.service.OrderService;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCleatingJob {

  private final OrderService orderService;

  @Scheduled(cron = "${scheduler.order-clearing-job-cron}")
  public void checkAndCancelExpiredOrders() {
    log.info("Run order clearing job");
    orderService.cancelExpiredOrders();
  }
}

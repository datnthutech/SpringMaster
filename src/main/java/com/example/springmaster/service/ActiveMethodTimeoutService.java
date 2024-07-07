package com.example.springmaster.service;

import com.example.springmaster.models.hotelInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActiveMethodTimeoutService {

    // region Main Method
    public List<hotelInfo> getHotels(List<Integer> lstDelay) {
        List<hotelInfo> hotels = new ArrayList<>();
        try {
            hotels = tryGetHotels(() -> getByRedis(lstDelay.get(0)), 200)
                    .thenCompose(result -> {
                        if(result.isEmpty()){
                            return tryGetHotels(() -> getByDbJoinReservation(lstDelay.get(1)), 200);
                        } else {
                            return CompletableFuture.completedFuture(result);
                        }
                    })
                    .thenCompose(result ->  {
                        if(result.isEmpty()){
                            return tryGetHotels(() -> getByNormal(lstDelay.get(2)), 2500);
                        } else {
                            return CompletableFuture.completedFuture(result);
                        }
                    })
                    .get();
        } catch(Exception e) {
            System.out.println("Exception | getHotels | " + Mono.just(e));
            return hotels;
        }

        System.out.println(Mono.just(hotels));
        return hotels;
    }
    // endregion

    // region Private Method

    private CompletableFuture<List<hotelInfo>> tryGetHotels(Supplier<CompletableFuture<List<hotelInfo>>> supplier, int timeout) {
        return supplier.get()
                .completeOnTimeout(List.of(), timeout, TimeUnit.MILLISECONDS);
    }

    private CompletableFuture<List<hotelInfo>> getByRedis(long timeoutMilliseconds) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(timeoutMilliseconds);
                return getTempHotelInfo(timeoutMilliseconds, "getByRedis");
            } catch (InterruptedException e) {
                return List.of();
            }
        });
    }

    private CompletableFuture<List<hotelInfo>> getByDbJoinReservation(int timeoutMilliseconds) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(timeoutMilliseconds);
                return getTempHotelInfo(timeoutMilliseconds, "getByDbJoinReservation");
            } catch (InterruptedException e) {
                return List.of();
            }
        });
    }

    private CompletableFuture<List<hotelInfo>> getByNormal(int timeoutMilliseconds) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(timeoutMilliseconds);
                return getTempHotelInfo(timeoutMilliseconds, "getByNormal");
            } catch (InterruptedException e) {
                return List.of();
            }
        });
    }

    // endregion

    // region Common Method
    private List<hotelInfo> getTempHotelInfo(long timeout, String byMethod){
        return new ArrayList<hotelInfo>(){{
            add(new hotelInfo(){{
                setHotelCode(1);
                setHotelName("Hotel VietNam 1-delay " + timeout+"-by"+byMethod);
                setCountryCode("VN");
                setCurrencyCode("VND");
            }});
            add(new hotelInfo(){{
                setHotelCode(2);
                setHotelName("Hotel Korean 2-delay " + timeout+"-by"+byMethod);
                setCountryCode("KR");
                setCurrencyCode("KRW");
            }});
            add(new hotelInfo(){{
                setHotelCode(3);
                setHotelName("Hotel Japan 3-delay " + timeout+"-by"+byMethod);
                setCountryCode("JP");
                setCurrencyCode("JPY");
            }});
            add(new hotelInfo(){{
                setHotelCode(4);
                setHotelName("Hotel Thailand 4-delay " + timeout+"-by"+byMethod);
                setCountryCode("TH");
                setCurrencyCode("THB");
            }});
            add(new hotelInfo(){{
                setHotelCode(5);
                setHotelName("Hotel Singapore 5-delay " + timeout+"-by"+byMethod);
                setCountryCode("SK");
                setCurrencyCode("SGD");
            }});
        }};
    }
    // endregion
}

package com.yolkin.test;

import com.yolkin.test.flow.EvenUserFlowGenerator;
import com.yolkin.test.flow.FixedUserFlowGenerator;
import com.yolkin.test.flow.UserFlowGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CashboxDispatcherUTest {

    private static Stream<Arguments> dataDispatch() {
        return Stream.of(
                Arguments.of(
                        // a new client comes each minute
                        // with default cashbox throughput
                        new EvenUserFlowGenerator(TimeUnit.MINUTES.toSeconds(5), 5),
                        CashboxFactory.produce(),
                        "43214"
                ),

                Arguments.of(
                        // a new client comes each minute within 10 minutes
                        // cashbox 4 is much better than others
                        new EvenUserFlowGenerator(TimeUnit.MINUTES.toSeconds(10), 10),
                        CashboxFactory.produce(new int[]{1, 2, 3, 20}),
                        "4324144444"
                ),

                Arguments.of(
                        // a new client comes each 1 minute within 5 minutes
                        // cashbox 4 can process a client every 2 minutes
                        new EvenUserFlowGenerator(TimeUnit.MINUTES.toSeconds(5), 5),
                        CashboxFactory.produce(new int[]{1, 2, 3, 30}),
                        "43424"
                ),

                Arguments.of(
                        // a new client comes every second within 25 seconds
                        // we imitate a case when most productive 4th cashbox reaches its limit and
                        // we have to redistribute clients in less performant queues
                        new FixedUserFlowGenerator(LongStream.range(0, 26).toArray()),
                        CashboxFactory.produce(new int[]{1, 2, 3, 60}),
                        "43214444444444444444444323"
                )
        );
    }


    @ParameterizedTest
    @MethodSource("dataDispatch")
    void testDispatch(UserFlowGenerator userFlowGenerator,
                      List<Cashbox> cashboxList,
                      String expectedOutput){

        StringBuilder builder = new StringBuilder();
        CashboxDispatcher dispatcher = new CashboxDispatcher(cashboxList);
        for (long time : userFlowGenerator.generate()) {
            builder.append(dispatcher.dispatch(time));
        }
        assertEquals(expectedOutput, builder.toString());
    }
}

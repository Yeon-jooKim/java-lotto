package lotto;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class FeatureTest {
    @Test
    void 입력을_숫자로_변환() {
        String input = "1";
        assertThat(Input.getInputToInt(input)).isEqualTo(1);
    }

    @Test
    void 입력을_숫자로_변환_예외_처리() {
        String input = "46";
        assertThatThrownBy(() -> Input.getInputToInt(input)).hasMessage("[ERROR] 로또 번호는 1부터 45 사이의 숫자여야 합니다.");
    }

    @Test
    void 입력_로또문자열을_리스트로_변환() {
        String input = "1,2,3,4,5,6";
        List<Integer> answer = List.of(1, 2, 3, 4, 5, 6);
        assertThat(Input.getWinningNumber(input)).isEqualTo(answer);
    }

    @Test
    void 로또_발행() {
        List<Integer> answer = User.getLottoNumbers();
        long count = answer.stream().distinct().count();
        assertThat(count).isEqualTo(6L);
    }

    @Test
    void 사용자의_구매횟수만큼_로또숫자_발행() {
        User user = new User("5000");
        user.getPurchaseLottoList();
        assertThat(user.purchaseLottoList.size()).isEqualTo(5);
    }

    @Test
    void 숫자_포함_여부_확인() {
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        int n = 1;
        int k = 7;
        assertThat(lotto.isContainNumber(n)).isEqualTo(ContainStatus.Contain);
        assertThat(lotto.isContainNumber(k)).isEqualTo(ContainStatus.NotContain);
    }

    @Test
    void 당첨갯수_확인() {
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        List<Integer> user = List.of(1, 2, 3, 4, 5, 6);
        assertThat(lotto.getMatchingNumber(user)).isEqualTo(6);
    }

    @Test
    void 당첨배열_인덱스_생성() {
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        Input input = new Input(7);
        List<List<Integer>> user = List.of(
                List.of(1, 2, 3, 4, 5, 6),
                List.of(1, 2, 3, 4, 5, 7),
                List.of(1, 2, 3, 4, 5, 8),
                List.of(1, 2, 3, 4, 8, 9),
                List.of(1, 2, 3, 10, 11, 12),
                List.of(1, 2, 11, 12, 13, 14),
                List.of(1, 11, 12, 13, 14, 15),
                List.of(11, 12, 13, 14, 15, 16)
        );
        List<WinningRank> answer = List.of(WinningRank.one, WinningRank.two, WinningRank.three
                , WinningRank.four, WinningRank.five, WinningRank.notThing, WinningRank.notThing
                , WinningRank.notThing
        );

        for (int i = 0; i < user.size(); i++) {
            assertThat(lotto.getWinningRank(user.get(i), input)).isEqualTo(answer.get(i));
        }

    }

    @Test
    void 당첨_맵_생성() {
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        Input input = new Input(7);
        List<List<Integer>> user = List.of(
                List.of(1, 2, 3, 4, 5, 6),
                List.of(1, 2, 3, 4, 5, 7),
                List.of(1, 2, 3, 4, 5, 8),
                List.of(1, 2, 3, 4, 8, 9),
                List.of(1, 2, 3, 10, 11, 12),
                List.of(1, 2, 11, 12, 13, 14),
                List.of(1, 11, 12, 13, 14, 15),
                List.of(11, 12, 13, 14, 15, 16)
        );
        List<Integer> bonus = List.of(10, 6, 23, 11, 10, 10, 15, 15);

        Map<WinningRank, Integer> enumMap = new EnumMap<WinningRank, Integer>(WinningRank.class);
        enumMap.put(WinningRank.one, 1);
        enumMap.put(WinningRank.two, 1);
        enumMap.put(WinningRank.three, 1);
        enumMap.put(WinningRank.four, 1);
        enumMap.put(WinningRank.five, 1);
        enumMap.put(WinningRank.notThing, 3);
        assertThat(lotto.getWinningResult(user, input)).isEqualTo(enumMap);
    }

    @Test
    void 수익_계산() {
        Calculator calculator = new Calculator();
        Map<WinningRank, Integer> enumMap = new EnumMap<WinningRank, Integer>(WinningRank.class);
        enumMap.put(WinningRank.one, 1);
        assertThat(calculator.getProfit(enumMap)).isEqualTo(WinningRank.one.getPrize());
    }

    @Test
    void 수익률_계산() {
        Calculator calculator = new Calculator();
        Map<WinningRank, Integer> enumMap = new EnumMap<WinningRank, Integer>(WinningRank.class);
        enumMap.put(WinningRank.five, 1);
        assertThat(calculator.getEarningsRate(8000, enumMap)).isEqualTo(62.5);
    }

    @Test
    void 입력_숫자_예외_처리_테스트() {
        String s = "1,2,3,4,5,z";
        assertThatThrownBy(() -> Input.getWinningNumber(s))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("[ERROR] 로또 번호 문자열에 숫자가 아닌 문자가 있습니다.");
        String s1 = "123,1,2,3,4,5";
        assertThatThrownBy(() -> Input.getWinningNumber(s1))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("[ERROR] 로또 번호는 1부터 45 사이의 숫자여야 합니다.");
        String s2 = "1,2,3,4,5";
        assertThatThrownBy(() -> Input.getWinningNumber(s2))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("[ERROR] 로또 번호의 숫자가 6개가 아닙니다.");
        String s3 = "1,2,3,4,5,5";
        assertThatThrownBy(() -> new Lotto(Input.getWinningNumber(s3)))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("[ERROR] 입력 숫자가 중복되었습니다.");
        String s4 = "1,2,3,4,5,6";
        int bonus = 6;
        Lotto lotto = new Lotto(Input.getWinningNumber(s4));
        assertThatThrownBy(() -> lotto.bonusNotIncludeWinningNumbers(bonus))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("[ERROR] 보너스 번호가 이미 로또번호에 포함되어 있습니다.");

    }

    @Test
    void 구매_금액_예외_처리() {

        assertThatThrownBy(() -> new User("5555"))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("[ERROR] 구매금액이 1000의 배수가 아닙니다.");
    }

    @Test
    void 구매목록_출력테스트() {
        View view = new View();
        List<List<Integer>> user = List.of(
                List.of(1, 2, 3, 4, 5, 6),
                List.of(1, 2, 3, 4, 5, 7),
                List.of(1, 2, 3, 4, 5, 8),
                List.of(1, 2, 3, 4, 8, 9),
                List.of(1, 2, 3, 10, 11, 12),
                List.of(1, 2, 11, 12, 13, 14),
                List.of(1, 11, 12, 13, 14, 15),
                List.of(11, 12, 13, 14, 15, 16)
        );
        OutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        view.printPurchaseList(user);
        String output = out.toString();
        assertThat(output).contains(
                "8개를 구매했습니다.",
                "[1, 2, 3, 4, 5, 6]",
                "[1, 2, 3, 4, 5, 7]",
                "[1, 2, 3, 4, 5, 8]",
                "[1, 2, 3, 4, 8, 9]",
                "[1, 2, 3, 10, 11, 12]",
                "[1, 2, 11, 12, 13, 14]",
                "[1, 11, 12, 13, 14, 15]",
                "[11, 12, 13, 14, 15, 16]"
        );

    }

    @Test
    void 당첨통계_출력() {
        View view = new View();

        OutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Map<WinningRank, Integer> enumMap = new EnumMap<WinningRank, Integer>(WinningRank.class);
        enumMap.put(WinningRank.one, 1);
        view.printWinningStatistics(enumMap);
        String output = out.toString();
        assertThat(output).contains(
                "당첨 통계",
                "---",
                "3개 일치 (5,000원) - 0개",
                "4개 일치 (50,000원) - 0개",
                "5개 일치 (1,500,000원) - 0개",
                "5개 일치, 보너스 볼 일치 (30,000,000원) - 0개",
                "6개 일치 (2,000,000,000원) - 1개"
        );
    }

    @Test
    void 수익률_출력() {
        View view = new View();
        OutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        view.printEarningsRate(62.5);
        String output = out.toString();
        assertThat(output).contains("총 수익률은 62.5%입니다.");
    }


}

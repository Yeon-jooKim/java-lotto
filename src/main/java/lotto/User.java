package lotto;

import camp.nextstep.edu.missionutils.Randoms;

import java.util.*;

public class User {
    public int inputCost;
    public List<List<Integer>> purchaseLottoList;
    public Map<WinningRank, Integer> winResult;

    User(String inputCost) {
        valid(inputCost);
        this.inputCost = Integer.parseInt(inputCost);
    }

    private void valid(String inputCost) {
        correctNumber(inputCost);
        multipleOfThousand(inputCost);
    }

    private void correctNumber(String inputCost) {
        for (int i = 0; i < inputCost.length(); i++) {
            char tempNum = inputCost.charAt(i);
            if (!(tempNum >= '0' && tempNum <= '9')) {
                throw new IllegalArgumentException("[ERROR] 구매금액이 숫자가 아닙니다.");
            }
        }
    }

    private void multipleOfThousand(String inputCost) {
        int cost = Integer.parseInt(inputCost);
        if (cost % 1000 != 0) {
            throw new IllegalArgumentException("[ERROR] 구매금액이 1000의 배수가 아닙니다.");
        }
    }

    public static List<Integer> getLottoNumbers() {
        List<Integer> result = new ArrayList<>(
                Randoms.pickUniqueNumbersInRange(1, 45, 6));
        result.sort(Comparator.naturalOrder());
        return result;
    }

    public void getPurchaseLottoList() {
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < inputCost / 1000; i++) {
            result.add(getLottoNumbers());
        }
        this.purchaseLottoList = result;
    }
}
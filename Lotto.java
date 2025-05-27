import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class Lotto extends JFrame {
    private final ArrayList<Integer> jadong = new ArrayList<>();
    private final ArrayList<Integer> input = new ArrayList<>();
    private final Set<JButton> selectedButtons = new HashSet<>();
    private final Random random = new Random();

    private JTextArea displayArea;

    public Lotto() {
        setTitle("로또 번호 생성기");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());

        // Display Area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // Button Panel for Numbers
        JPanel numberPanel = new JPanel(new GridLayout(5, 9, 5, 5));
        for (int i = 1; i <= 45; i++) {
            JButton button = new JButton(String.valueOf(i));
            button.addActionListener(new NumberButtonListener(button));
            numberPanel.add(button);
        }
        add(numberPanel, BorderLayout.NORTH);

// Control Panel (3 buttons on the first row and 4 buttons on the second row)
JPanel controlPanel = new JPanel(new GridLayout(2, 4, 10, 10)); // 2 rows, 4 buttons

JButton generateButton = new JButton("번호 자동 생성");
generateButton.addActionListener(e -> generateNumbers());

JButton checkButton = new JButton("당첨 여부 확인");
checkButton.addActionListener(e -> checkResults());

JButton saveButton = new JButton("번호 저장");
saveButton.addActionListener(e -> saveNumbers());

JButton loadButton = new JButton("번호 불러오기");
loadButton.addActionListener(e -> loadNumbers());

JButton customGenerateButton = new JButton("맞춤형 번호 생성");
customGenerateButton.addActionListener(e -> generateCustomNumbers());

JButton oddsButton = new JButton("당첨 확률");
oddsButton.addActionListener(e -> simulateLottoOdds());

JButton miniGameButton = new JButton("미니 게임");
miniGameButton.addActionListener(e -> startMiniGame());

// 초기화 버튼 생성
JButton resetButton = new JButton("초기화");
resetButton.addActionListener(e -> {
    // 선택된 버튼들의 배경색을 원래대로 돌리고, 선택 목록을 비웁니다.
    for (JButton btn : selectedButtons) {
        btn.setBackground(null); // 선택된 버튼의 배경색을 초기화
    }
    selectedButtons.clear(); // 선택된 버튼 목록 초기화
    input.clear();           // 입력 리스트 초기화

    // 미니게임에서 선택된 번호의 버튼도 초기화
    for (Component comp : getContentPane().getComponents()) {
        if (comp instanceof JPanel) {
            JPanel panel = (JPanel) comp;
            for (Component buttonComp : panel.getComponents()) {
                if (buttonComp instanceof JButton) {
                    JButton button = (JButton) buttonComp;
                    button.setBackground(null); // 모든 버튼의 배경색을 원래대로 돌림
                }
            }
        }
    }

    displayArea.append("선택한 번호가 초기화되었습니다.\n"); // 초기화 메시지 출력
});


// controlPanel에 초기화 버튼 추가
controlPanel.add(resetButton);

controlPanel.add(generateButton);
controlPanel.add(checkButton);
controlPanel.add(saveButton);

controlPanel.add(loadButton);
controlPanel.add(customGenerateButton);
controlPanel.add(oddsButton);
controlPanel.add(miniGameButton);
        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void generateNumbers() {
        jadong.clear();
        while (jadong.size() < 6) {
            int num = random.nextInt(45) + 1;
            if (!jadong.contains(num)) {
                jadong.add(num);
            }
        }
        Collections.sort(jadong); // 번호만 생성하고 출력은 하지 않음
    }
    

    private void checkResults() {
        if (jadong.isEmpty()) { // 번호가 생성되지 않았으면 생성
            generateNumbers();
        }
        
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "번호를 선택해야 합니다.");
            return;
        }
    
        displayArea.append("자동 생성된 번호: " + jadong + "\n"); // 당첨 여부 확인할 때 번호 출력
    
        Set<Integer> match = new HashSet<>(jadong);
        match.retainAll(input);
        int count = match.size();
    
        String result;
        switch (count) {
            case 6 -> result = "1등 (6개 일치)";
            case 5 -> result = "2등 (5개 일치)";
            case 4 -> result = "3등 (4개 일치)";
            case 3 -> result = "4등 (3개 일치)";
            default -> result = "낙첨 (2개 이하 일치)";
        }
        displayArea.append("당첨 여부: " + result + "\n일치하는 번호: " + match + "\n");
    }
    

    private void saveNumbers() {
        if (jadong.isEmpty() || input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "번호를 생성하거나 선택해야 합니다.");
            return;
        }

        String filename = JOptionPane.showInputDialog("저장할 파일 이름을 입력하세요:");
        if (filename != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                writer.write("자동 생성 번호: " + jadong + "\n");
                writer.write("사용자 입력 번호: " + input + "\n");
                displayArea.append("번호가 " + filename + " 파일에 저장되었습니다.\n");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "파일 저장 중 오류가 발생했습니다.");
            }
        }
    }

    private void loadNumbers() {
        String filename = JOptionPane.showInputDialog("불러올 파일 이름을 입력하세요:");
        if (filename != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                displayArea.append("불러온 번호:\n");
                String line;
                while ((line = reader.readLine()) != null) {
                    displayArea.append(line + "\n");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "파일을 불러오는 중 오류가 발생했습니다.");
            }
        }
    }

    private void generateCustomNumbers() {
        if (input.isEmpty()) {
            // 사용자가 번호를 선택하지 않은 경우, 자동으로 6개의 번호 생성
            Set<Integer> recommendedNumbers = new HashSet<>();
            while (recommendedNumbers.size() < 6) {
                int num = random.nextInt(45) + 1;
                recommendedNumbers.add(num);
            }
    
            // 번호를 정렬하여 출력
            List<Integer> sortedRecommendedNumbers = new ArrayList<>(recommendedNumbers);
            Collections.sort(sortedRecommendedNumbers);
    
            displayArea.append("추천 번호: " + sortedRecommendedNumbers + "\n");
        } else {
            // 사용자가 번호를 선택한 경우, 빈도 기반 추천
            Map<Integer, Integer> frequencyMap = new HashMap<>();
            for (int num : input) {
                frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
            }
    
            // 빈도 높은 순으로 번호 정렬
            List<Integer> recommendedNumbers = new ArrayList<>(frequencyMap.keySet());
            Collections.sort(recommendedNumbers, (a, b) -> frequencyMap.get(b) - frequencyMap.get(a));
    
            // 6개 번호만 추천
            List<Integer> customNumbers = new ArrayList<>();
            for (int i = 0; i < 6 && i < recommendedNumbers.size(); i++) {
                customNumbers.add(recommendedNumbers.get(i));
            }
    
            displayArea.append("맞춤형 번호 추천: " + customNumbers + "\n");
        }
    }
    
    private void simulateLottoOdds() {
        // 총 가능한 조합 수는 C(45, 6) => 8145060
        int totalCombinations = 8145060;
    
        // 1등: 6개 번호 일치
        double firstPrizeOdds = 1.0 / totalCombinations;
    
        // 2등: 5개 번호 일치, 보너스 번호 일치
        double secondPrizeOdds = 1.0 / (totalCombinations / 45.0);
    
        // 3등: 5개 번호 일치
        double thirdPrizeOdds = 1.0 / (totalCombinations / 45.0);
    
        // 4등: 4개 번호 일치
        double fourthPrizeOdds = 1.0 / (totalCombinations / 45.0);
    
        // 확률을 백분율로 표시 (확률에 100을 곱하여 백분율로 변환)
        displayArea.append("1등 당첨 확률: 1/" + totalCombinations + " (" + String.format("%.8f", firstPrizeOdds * 100) + "%)\n");
        displayArea.append("2등 당첨 확률: 1/" + (totalCombinations / 45.0) + " (" + String.format("%.8f", secondPrizeOdds * 100) + "%)\n");
        displayArea.append("3등 당첨 확률: 1/" + (totalCombinations / 45.0) + " (" + String.format("%.8f", thirdPrizeOdds * 100) + "%)\n");
        displayArea.append("4등 당첨 확률: 1/" + (totalCombinations / 45.0) + " (" + String.format("%.8f", fourthPrizeOdds * 100) + "%)\n");
    }
    
    

    private void startMiniGame() {
        int correctNumber = random.nextInt(45) + 1;
        boolean guessedCorrectly = false;
    
        while (!guessedCorrectly) {
            String answer = JOptionPane.showInputDialog("1부터 45까지의 숫자 중 하나를 맞춰보세요!");
    
            if (answer == null) {
                // 입력 창이 닫히거나 취소되면 반복을 종료합니다.
                JOptionPane.showMessageDialog(this, "게임을 종료합니다.");
                break;
            }
    
            try {
                int guessedNumber = Integer.parseInt(answer);
                if (guessedNumber == correctNumber) {
                    input.add(correctNumber); // 번호가 맞았을 때 'input' 리스트에 추가
                    // 번호가 추가된 후, 화면에 알림
                    displayArea.append("정답입니다! 번호 " + correctNumber + "이 추가되었습니다.\n");
                    displayArea.append("현재 선택된 번호: " + input + "\n"); // 현재 입력된 번호를 표시
    
                    // 해당 버튼의 배경색을 변경하여 화면에 선택된 번호를 표시
                    for (Component comp : getContentPane().getComponents()) {
                        if (comp instanceof JPanel) {
                            JPanel panel = (JPanel) comp;
                            for (Component buttonComp : panel.getComponents()) {
                                if (buttonComp instanceof JButton) {
                                    JButton button = (JButton) buttonComp;
                                    int num = Integer.parseInt(button.getText());
                                    if (num == correctNumber) {
                                        button.setBackground(Color.CYAN); // 선택된 번호는 CYAN 색으로 강조
                                    }
                                }
                            }
                        }
                    }
    
                    guessedCorrectly = true;  // 정답을 맞추면 반복을 종료
                } else {
                    JOptionPane.showMessageDialog(this, "틀렸습니다. 다시 시도해주세요.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "유효한 숫자를 입력해주세요.");
            }
        }
    }    
    
    

    private class NumberButtonListener implements ActionListener {
        private final JButton button;

        public NumberButtonListener(JButton button) {
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int num = Integer.parseInt(button.getText());
            if (selectedButtons.contains(button)) {
                selectedButtons.remove(button);
                input.remove(Integer.valueOf(num));
                button.setBackground(null);
            } else if (input.size() < 6) {
                selectedButtons.add(button);
                input.add(num);
                button.setBackground(Color.CYAN);
            } else {
                JOptionPane.showMessageDialog(Lotto.this, "이미 6개의 번호를 선택했습니다.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Lotto::new);
    }
}
package MyProject;

import java.util.*;
import java.io.*;

public class RoDDo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Integer> jadong = new ArrayList<>(); // 자동으로 생성되는 번호를 담는다.
        List<Integer> input = new ArrayList<>();  // 직접 입력하는 번호를 담는다.
        Random random = new Random();

        while (true) {
            System.out.println("\n===== 로또 번호 생성기 =====");
            System.out.println("\n1. 로또 번호 직접 입력");
            System.out.println("2. 로또 번호 생성하기");
            System.out.println("3. 당첨 여부 확인");
            System.out.println("4. 번호 저장");
            System.out.println("5. 번호 불러오기");
            System.out.println("6. 종료");
            System.out.print("\n번호를 입력해주세요 : ");

            String choice = sc.next();

            // 번호를 직접 입력할 때
            if ( choice.equals("1") ) {
                input.clear();
                System.out.println("원하는 로또 번호 6개를 입력해주세요! ( 1 ~ 45 사이의 숫자 ) : ");

                while ( input.size() < 6 ) {
                    try {
                        int num = sc.nextInt();
                        if ( num >= 1 && num <= 45 && !input.contains(num) ) {
                            input.add(num);
                        } else {
                            System.out.println("1~45 사이의 숫자여야하고, 중복이 아니여야합니다! 다시 입력해주세요");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("숫자를 입력하세요.");
                        sc.next(); // 잘못된 입력을 했을 경우
                    }
                }

                Collections.sort(input);
                System.out.println("입력한 로또 번호 : " + input);

            // 자동으로 번호 생성
            } else if ( choice.equals("2") ) {
                jadong.clear();
                while ( jadong.size() < 6 ) {
                    int num = random.nextInt(45) + 1;
                    if ( !jadong.contains(num) ) {
                        jadong.add(num);
                    }
                }

                Collections.sort(jadong);
                System.out.println("자동 생성된 로또 번호 : " + jadong);

            // 당첨인지 아닌지 여부 확인하기
            } else if ( choice.equals("3") ) {
                if ( !jadong.isEmpty() && !input.isEmpty() ) {
                    Set<Integer> match = new HashSet<>(jadong);
                    match.retainAll(input); // 교집합인지 계산합니당
                    int count = match.size();

                    if ( count == 6 ) {
                        System.out.println("당첨 여부 : 1등 ( 6개 일치 )");
                    } else if ( count == 5 ) {
                        System.out.println("당첨 여부 : 2등 ( 5개 일치 )");
                    } else if ( count == 4 ) {
                        System.out.println("당첨 여부 : 3등 ( 4개 일치 )");
                    } else if ( count == 3 ) {
                        System.out.println("당첨 여부 : 4등 ( 3개 일치 )");
                    } else {
                        System.out.println("당첨 여부 : 낙첨 ( 2개 이하 일치 )");
                    }
                    System.out.println("일치하는 번호 : " + match);
                    
                } else {
                    System.out.println("3번을 실행하기 전에 번호를 생성하거나 입력하세요!");
                }

            // 번호를 저장합니당
            } else if ( choice.equals("4") ) {
                if ( !jadong.isEmpty() && !input.isEmpty() ) {
                    System.out.print("저장할 파일 이름을 입력하세요: ");
                    String filename = sc.next();

                    try ( BufferedWriter writer = new BufferedWriter(new FileWriter(filename)) ) {
                        writer.write("자동 생성 번호: " + jadong + "\n");
                        writer.write("사용자 입력 번호: " + input + "\n");
                        System.out.println("번호가 " + filename + " 파일에 저장되었습니다.");
                    } catch (IOException e) {
                        System.out.println("파일 저장 중 오류가 발생했습니다.");
                    }
                } else {
                    System.out.println("4번을 실행하기 전에 번호를 생성하거나 입력하세요!");
                }

            // 저장했던 번호를 불러옵니당
            } else if ( choice.equals("5") ) {
                System.out.print("불러올 파일 이름을 입력하세요 : ");
                String filename = sc.next();

                try ( BufferedReader reader = new BufferedReader(new FileReader(filename)) ) {
                    String line;
                    while ( (line = reader.readLine()) != null ) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    System.out.println("파일을 불러오는 중 오류가 발생했습니다.");
                }

            // 종료
            } else if ( choice.equals("6") ) {
                System.out.println("프로그램을 종료합니다.");
                break;

            // 예외의 입력
            } else {
                System.out.println("잘못된 입력을 하셨습니다. 다시 입력해주세요!");
            }
        }
    }
}

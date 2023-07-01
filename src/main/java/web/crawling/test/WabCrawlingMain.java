package web.crawling.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.FileWriter;
import java.util.List;
import java.util.ArrayList;

import java.io.IOException ;

import static java.lang.Thread.sleep;

public class WabCrawlingMain {

    public static void main(String[]  args) {

        List<String> list = new ArrayList<>();
        List<String> tiLIst = new ArrayList<>();

        String strQ = null;
        String strBogi = null;
        String strBogi4 = null;
        String URL = null ;

        String filePath = "C:/ExamFile/ExamTest.txt"; // 파일 경로

        for (int i = 102170; i <= 102200; i++) {
            try {
                URL = "https://www.examtopics.com/discussions/amazon/view/"+ i +"-exam-aws-certified-solutions-architect-associate-saa-c03/" ;
                Document doc = Jsoup.connect(URL).get();

                String target = "SAA-C03";
                String title = doc.title();
                // 추출한 문제가 SAA C03 이 아닐경우 skip
                if (!title.contains(target)) {
                    System.out.println(target + " 이 아니잖아!");
                    sleep(500);
                    continue;
                }
                System.out.println("Title : " + title);

//                String[] splitTitle = title.split("question") ;
//                String qTitle = splitTitle[1];
//                tiLIst.add(qTitle);
                tiLIst.add(title);

                String text = doc.text() ;

                String[] strSplit = text.split("]") ;

                //  요기까지가 문제와 답
                String[] strSplit2 = strSplit[2].split("Show Suggested");

                // 문항 사이에 엔터 입력
                String[] strList = strSplit2[0].split("A\\.") ;

                strQ = strList[0] ;
                //System.out.println("문제 : \n" + strQ);
                strBogi = "A." + strList[1] ;
                String strBogi2 = strBogi.replace("B.","\nB.") ;
                String strBogi3 = strBogi2.replace("C.","\nC.") ;
                strBogi4 = strBogi3.replace("D.","\nD.") ;

                // Answer
//                String strAnswer = strList[]


            }catch (Exception e) {
                e.printStackTrace();

            }

            String strSum = strQ + "\n" + "\n" +  strBogi4;

            System.out.println(strSum);


            try {
                System.setProperty("webdriver.chrome.driver", "C:\\Users\\pin\\Downloads\\chromedriver_win32\\chromedriver.exe");

                WebDriver driver = new ChromeDriver();
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                driver.get("https://papago.naver.com/");
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("파파고 : " + driver.getTitle());
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // 텍스트 입력
                String textToTranslate = strSum;
                WebElement textSourceInput = driver.findElement(By.cssSelector("textarea#txtSource"));
                textSourceInput.sendKeys(textToTranslate);
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // 버튼 클릭
                WebElement translateButton = driver.findElement(By.cssSelector("button#btnTranslate"));
                translateButton.click();
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // 번역 결과
                WebElement translationResult = driver.findElement(By.cssSelector("div#txtTarget"));
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                String translatedText = translationResult.getText();
                System.out.println("\n==============");
                System.out.println("Question : \n" + translatedText);
                System.out.println("==============");

                list.add(translatedText);




                // WebDriver 종료
                driver.quit();

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        /**
         * 크롤링 종료
         * */


        int listCount = 0;
        for (String row : list) {
            try (FileWriter writer = new FileWriter(filePath, true)) {
                String content = row; // 파일에 쓸 내용

                writer.append( tiLIst.get(listCount) + "\n"+ "\n" + content + "\n" + "\n" + "\n"); // 파일에 내용 쓰기
                listCount++;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }



    }//end main

}//end class

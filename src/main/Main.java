package main;

import util.HashOfValue;
import util.ImagePHash;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input path to directory: ");
        String s = scanner.nextLine();
        File directory = new File(s);

        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                BufferedImage image = ImageIO.read(file);
                int cardsOnTable = calculationOfCards(image);
                int xValueOfCard = 146;
                int yValueOfCard = 589;
                int xSuiteOfCard = 169;
                int ySuiteOfCard = 638;

                for (int i = 0; i < cardsOnTable; i++) {
                    BufferedImage imgSuiteOfCard = image.getSubimage(xSuiteOfCard, ySuiteOfCard, 30, 30);
                    ImagePHash imagePHash = new ImagePHash();

                    int rgbOfSuite = imgSuiteOfCard.getRGB(15, 15);
                    String suites = getSuites(imgSuiteOfCard, rgbOfSuite);
                    xSuiteOfCard += 72;
                    BufferedImage imgValueOfCard = image.getSubimage(xValueOfCard, yValueOfCard, 32, 32);

                    String hash = imagePHash.getHash(imgValueOfCard);
                    String valueOfCard = getValueOfCard(hash);
                    System.out.print(valueOfCard + suites);
                    xValueOfCard += 72;
                }
                System.out.println("");
            }
        } else {
            throw new IOException("Wrong directory specified " + directory);
        }
    }

    private static int calculationOfCards(BufferedImage image) {
        int cardsOnTable = 0;
        int xOfCard = 173;
        int yOfCard = 588;
        for (int i = 0; i < 5; i++) {
            int rgb = image.getRGB(xOfCard, yOfCard);
            if (rgb == -1 || rgb == -8882056) {
                cardsOnTable++;
                xOfCard = xOfCard + 76;
            }
        }
        return cardsOnTable;
    }

    private static String getSuites(BufferedImage image, int rgb) {
        String suite = "";
        switch (rgb) {
            case -10477022:
            case -3323575:
                suite = getRedSuite(image);
                break;
            case -14474458:
            case -15724526:
                suite = getBlackSuite(image);
                break;
        }
        return suite;
    }

    private static String getRedSuite(BufferedImage image) {
        String suite = "";
        int firstColor;
        int secondColor;
        for (int i = 0; i < 8; i++) {
            firstColor = image.getRGB(15, 12 - i);
            secondColor = image.getRGB(15, 12 - i - 1);
            if (firstColor != secondColor) {
                suite = "h";
                break;
            } else {
                suite = "d";
            }
        }
        return suite;
    }

    private static String getBlackSuite(BufferedImage image) {
        String suite = "";
        int firstColor;
        int secondColor;
        for (int i = 0; i < 8; i++) {
            firstColor = image.getRGB(16 - i, 7);
            secondColor = image.getRGB(16 - i - 1, 7);
            if (firstColor != secondColor) {
                suite = "c";
                break;
            } else {
                suite = "s";
            }
        }
        return suite;
    }


    private static String getValueOfCard(String s) {
        int min = 100;
        String valueOfCard = "";
        for (HashOfValue s1 : HashOfValue.values()) {
            int i = calculateHammingDistance(s, s1.getValue());
            if (min > i) {
                min = i;
                valueOfCard = s1.getName();
            }
        }
        return valueOfCard;
    }

    static private int calculateHammingDistance(String One, String Two) {
        if (One.length() != Two.length())
            return -1;
        int counter = 0;
        for (int i = 0; i < One.length(); i++) {
            if (One.charAt(i) != Two.charAt(i)) counter++;
        }
        return counter;
    }
}

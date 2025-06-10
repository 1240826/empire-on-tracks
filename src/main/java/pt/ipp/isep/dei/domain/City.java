package pt.ipp.isep.dei.domain;

import java.util.*;
import java.util.Random;

public class City {
    private String name;
    private int positionX;
    private int positionY;
    private int houseBlockCount;
    private String assignmentMethod;
    private List<HouseBlock> houseBlocks = new ArrayList<>();
    private static final Random random = new Random();

    public City(String name, int positionX, int positionY, int houseBlockCount, String assignmentMethod) {
        this.name = name;
        this.positionX = positionX;
        this.positionY = positionY;
        this.houseBlockCount = houseBlockCount;
        this.assignmentMethod = assignmentMethod;
    }

    public String getName() {
        return name;
    }

    public void validateName() {
        if (!name.matches("[a-zA-Z ]+")) {
            throw new IllegalArgumentException("Invalid city name.");
        }
    }

    public void validateHouseBlocks() {
        if (houseBlockCount <= 1) {
            throw new IllegalArgumentException("House block count must be greater than 1.");
        }
    }

    public void createHouseBlocksManually(List<Integer> posX, List<Integer> posY) {
        for (int i = 0; i < houseBlockCount; i++) {
            HouseBlock block = new HouseBlock(posX.get(i), posY.get(i));
            addHouseBlock(block);
        }
    }

    public void createHouseBlocksAutomatically() {
        for (int i = 0; i < houseBlockCount; i++) {
            // Gerar a posição do house block com distribuição normal
            double offsetX = computeRelativePositionX(i);
            double offsetY = computeRelativePositionY(i);

            // A posição do house block será a posição da cidade + o offset calculado pela distribuição normal
            int finalX = (int) (positionX + offsetX);
            int finalY = (int) (positionY + offsetY);

            // Criar e adicionar o HouseBlock à lista
            HouseBlock block = new HouseBlock(finalX, finalY);
            addHouseBlock(block);
        }
    }

    // Geração de valores com distribuição normal para a posição X (centrada na posição da cidade)
    private double computeRelativePositionX(int index) {
        double mean = 0;       // Média (centro da cidade em X)
        double stddev = 2;     // Desvio padrão pequeno (2)

        // Gerar número com base em uma distribuição normal centrada na posição X da cidade
        return random.nextGaussian() * stddev;
    }

    // Geração de valores com distribuição normal para a posição Y (centrada na posição da cidade)
    private double computeRelativePositionY(int index) {
        double mean = 0;       // Média (centro da cidade em Y)
        double stddev = 2;     // Desvio padrão pequeno (2)

        // Gerar número com base em uma distribuição normal centrada na posição Y da cidade
        return random.nextGaussian() * stddev;
    }
    public void addHouseBlock(HouseBlock block) {
        houseBlocks.add(block);
    }
}


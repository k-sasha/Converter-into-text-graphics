package ru.netology.graphics.image;

public class Schema implements TextColorSchema {

    char[] colors = {'▇', '●', '◉', '◍', '◎', '○', '☉', '◌', '-'};

    @Override
    public char convert(int color) {
        if (color < 0 || color > 255) {
            System.out.println("Цвет должен быть в пределах от 0 до 255");
        }
        int first = 0;
        int second = 28;
        int count = 0;
        while (second <= second * colors.length) {
            if (color >= first && color < second) {
                return colors[count];
            }
            first += 28;
            second += 28;
            count++;
        }
        return colors[8];
    }
}

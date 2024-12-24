package mainPackage;

public class Cocktail extends Food {

    private String drink;
    private String fruit;

    public void consume() {
        System.out.println(this + " выпит");
    }

    public Cocktail(String drink, String fruit) {
        super("Коктейль");
        this.drink = drink;
        this.fruit = fruit;
    }

        public String getDrink()
        {
            return drink;
        }

        public String getFruit()
        {
            return fruit;
        }

    @Override
    public int calculateCalories() {
        int calories = 0;
        if (drink.equals("молоко")) {
            calories += 40;
        } else if (drink.equals("вода")) {
            calories += 1;
        }else if(drink.equals("кола")){
            calories+=500;
        }
        if (fruit.equals("рябина")) {
            calories += 33;
        } else if (fruit.equals("клубника")) {
            calories += 28;
        }else if(fruit.equals("мандарин")){
            calories+=50;
        }else {
            calories+= 10;
        }
        return calories;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (!(obj instanceof Cocktail)) return false;
        return (drink.equals(((Cocktail) obj).drink) && (fruit.equals(((Cocktail) obj).fruit)));
    }

    public String toString() {
        return super.toString() + " c добавкой '" + drink.toUpperCase() + "'" + " и '" + fruit.toUpperCase() + "'";
    }
}

package mainPackage;

public abstract class Food implements Consumable, Nutritious{
    private String name;

    public Food(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Food)) return false;
        if(name==null || ((Food)obj).name==null) return false;
        return name.equals(((Food)obj).name);
    }

    @Override
    public String toString() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

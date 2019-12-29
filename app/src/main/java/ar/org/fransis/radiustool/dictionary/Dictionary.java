package ar.org.fransis.radiustool.dictionary;

public class Dictionary {

    private static Dictionary instance = null;

    private Dictionary(){

    }

    public static Dictionary getInstance(){
        if(instance == null){
            instance = new Dictionary();
        }
        return instance;
    }
/*
    public boolean loadDictionary(String filename){
        Files.lines(Paths.get(filename))
                .filter(line -> !line.startsWith("#"))
                .map(line -> {
                    if(!line.startsWith("#")){
                        String s[] = line.split("\t{1,}");

                    }
                    return
                });
        return true;
    }
*/
}


class Attribute {
    private int id;
    private String value;
    private String type;

    public Attribute(int id, String value, String type) {
        this.id = id;
        this.value = value;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
}
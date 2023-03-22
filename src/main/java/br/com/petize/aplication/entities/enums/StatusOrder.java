package br.com.petize.aplication.entities.enums;

public enum StatusOrder {
    PENDENTE("pendente",0),
    PROCESSANDO("processando",1),
    CONCLUIDO("concluído",2) ;

    private final String name;
    private final int id;

    private StatusOrder(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public static String returnName(int id) {
        for (StatusOrder e : values()) {
            if (e.getId() == (id))
                return e.name();
        }
        return "";
    }
    public static int returnId(String s) {
        for (StatusOrder e : values()) {
            String value=e.getName().toUpperCase();
            if (value.equals(s.toUpperCase()))
                return e.getId();
        }
        return -1;
    }
}

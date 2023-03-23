package br.com.petize.aplication.model.enums;

/**
 * Esta enumeração representa os possíveis status de uma ordem. * <p>
 * Cada status é definido por um nome e um identificador numérico.
 * Também inclui métodos para retornar o nome e o identificador de um status com base no seu valor numérico ou nome. * <p>
 * Os possíveis status são: PENDENTE, PROCESSANDO e CONCLUÍDO.
 *
 * @author [Marcelo Macedo]
 */
public enum StatusOrder {
    PENDENTE("pendente", 0),
    PROCESSANDO("processando", 1),
    CONCLUÍDO("concluído", 2);

    private final String name;
    private final int id;

    /**
     * Construtor da enumeração.
     *
     * @param name o nome do status
     * @param id   o identificador numérico do status
     */
    StatusOrder(String name, int id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Retorna o identificador numérico do status.
     *
     * @return o identificador numérico do status
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna o nome do status.
     *
     * @return o nome do status
     */
    public String getName() {
        return name;
    }

    /**
     * Retorna o nome do status com base no seu identificador numérico.
     *
     * @param id o identificador numérico do status
     * @return o nome do status correspondente ao identificador numérico fornecido
     */
    public static String returnName(int id) {
        for (StatusOrder e : values()) {
            if (e.getId() == (id))
                return e.name();
        }
        return "";
    }

    /**
     * Retorna o identificador numérico do status com base no seu nome.
     *
     * @param s o nome do status
     * @return o identificador numérico do status correspondente ao nome fornecido
     */
    public static int returnId(String s) {
        for (StatusOrder e : values()) {
            String value = e.getName().toUpperCase();
            if (value.equals(s.toUpperCase()))
                return e.getId();
        }
        return -1;
    }
}
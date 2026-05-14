package dgtic.core.system.model.enums;

public enum Color {
    ROJO("#BF0A2E"),
    AZUL("#0A19BF"),
    AMARILLO("#F5D238"),
    VERDE("#4BAE24"),
    ROSA("#C638D6"),
    MORADO("#6238D6"),
    NARANJA("#D67238"),
    TURQUESA("#38D3D6");

    private final String codigo;

    Color(String codigo) {
        this.codigo = codigo;
    }
    public String getCodigo() {
        return codigo;
    }

}

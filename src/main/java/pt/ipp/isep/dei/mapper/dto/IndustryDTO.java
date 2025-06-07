package pt.ipp.isep.dei.mapper.dto;

/**
 * DTO used by the console menu.
 * Fields: index, name, cargo consumed (type & amount), cargo produced (type & amount).
 */
public final class IndustryDTO {

    private final int    index;
    private final String name;

    private final String consumesType;   // "–" if none
    private final double consumesQty;    // 0 if none

    private final String producesType;
    private final double producesQty;

    public IndustryDTO(int    index,
                       String name,
                       String consumesType,
                       double consumesQty,
                       String producesType,
                       double producesQty) {
        this.index        = index;
        this.name         = name;
        this.consumesType = consumesType;
        this.consumesQty  = consumesQty;
        this.producesType = producesType;
        this.producesQty  = producesQty;
    }

    public int    getIndex()        { return index; }
    public String getName()         { return name;  }
    public String getConsumesType() { return consumesType; }
    public double getConsumesQty()  { return consumesQty;  }
    public String getProducesType() { return producesType; }
    public double getProducesQty()  { return producesQty;  }

    @Override public String toString() { return name; }
}

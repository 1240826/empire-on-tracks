package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.domain.Location;
import pt.ipp.isep.dei.domain.Cargo;

/**
 * Interface comum para todos os tipos de indústria.
 */
public interface Industry {

    /** Nome da indústria */
    String getIndustryName();

    /** Custo fixo de operação da indústria */
    int getIndustryCost();

    /** Taxa de produção mensal */
    double getProduction();

    /** Demanda mensal para processo (quando aplicável) */
    double getDemand();

    /** Localização no mapa */
    void setLocation(Location location);
    Location getLocation();

    /** Executa um ciclo de produção/transformação */
    void produceCargo();

    /** Consome a quantidade especificada do estoque de entrada (quando aplicável) */
    void consumeCargo(double quantity);

    /** Retorna o objeto Cargo associado (geralmente o estoque de saída) */
    Cargo getCargo();

    /** Quantidade em estoque do Cargo retornado por getCargo() */
    default double getCargoQuantity() {
        return getCargo().getQuantity();
    }
}

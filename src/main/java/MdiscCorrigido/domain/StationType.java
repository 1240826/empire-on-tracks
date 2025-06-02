package MdiscCorrigido.domain;

/**
 * Tipo de estação:
 *   D* → DEPOT
 *   T* → TERMINAL
 *   S* (ou outro) → STATION
 */
public enum StationType {
    DEPOT, STATION, TERMINAL;

    public static StationType fromName(String name) {
        if (name == null || name.isEmpty()) return STATION;
        char prefix = name.charAt(0);
        if (prefix == 'D') return DEPOT;
        if (prefix == 'T') return TERMINAL;
        return STATION;
    }
}

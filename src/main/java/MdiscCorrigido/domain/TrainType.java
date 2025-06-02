package MdiscCorrigido.domain;

/**
 * Tipos de comboio (usado para filtrar electrificação em canTravel):
 *  ELECTRIC – só usa linhas electrificadas
 *  DIESEL  – pode usar qualquer linha
 *  STEAM   – pode usar qualquer linha
 */
public enum TrainType {
    ELECTRIC,
    DIESEL,
    STEAM;
}

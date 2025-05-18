package pt.ipp.isep.dei.domain;

/**
 * Defines the various types of cargo handled within the simulation.
 * <p>
 * CargoType enumerates all supported resource categories that industries,
 * stations, and locomotives can consume, produce, or transport.
 * </p>
 */
public enum CargoType {

    /** Electrical energy cargo. */
    ELECTRICITY,

    /** Coal resource cargo. */
    COAL,

    /** Steel resource cargo. */
    STEEL,

    /** Food resource cargo. */
    FOOD,

    /** Technology goods cargo. */
    TECHNOLOGY,

    /** Oil resource cargo. */
    OIL,

    /** Gas resource cargo. */
    GAS,

    /** Plastic resource cargo. */
    PLASTIC,

    /** Passenger transport cargo. */
    PASSENGERS,

    /** Mail and parcel transport cargo. */
    MAIL
}
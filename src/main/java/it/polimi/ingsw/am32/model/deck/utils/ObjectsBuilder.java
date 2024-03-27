package it.polimi.ingsw.am32.model.deck.utils;

import it.polimi.ingsw.am32.model.card.CornerType;
import it.polimi.ingsw.am32.model.card.pointstrategy.*;

/**
 * This class contains methods to convert strings to enum values and to build PointStrategy objects from strings.
 *
 * @author Lorenzo
 */
public class ObjectsBuilder {
    /**
     * Converts a string to its corresponding CornerType enum value.
     *
     * @param str the string to be converted. It should match one of the CornerType enum values.
     * @return The corresponding CornerType enum value, if the string matches, null if it doesn't.
     * @author Lorenzo
     */
    protected CornerType stringToCornerType(String str) {
        return switch (str) {
            case "PLANT" -> CornerType.PLANT;
            case "FUNGI" -> CornerType.FUNGI;
            case "ANIMAL" -> CornerType.ANIMAL;
            case "INSECT" -> CornerType.INSECT;
            case "QUILL" -> CornerType.QUILL;
            case "INKWELL" -> CornerType.INKWELL;
            case "MANUSCRIPT" -> CornerType.MANUSCRIPT;
            case "EMPTY" -> CornerType.EMPTY;
            case "NON_COVERABLE" -> CornerType.NON_COVERABLE;
            default -> null;
        };
    }

    /**
     * Converts a string to its corresponding ObjectType enum value.
     *
     * @param str the string to be converted. It should match one of the ObjectType enum values.
     * @return The corresponding ObjectType enum value, if the string matches, null if it doesn't.
     * @author Lorenzo
     */
    protected ObjectType stringToObjectType(String str) {
        return switch (str) {
            case "PLANT" -> ObjectType.PLANT;
            case "FUNGI" -> ObjectType.FUNGI;
            case "ANIMAL" -> ObjectType.ANIMAL;
            case "INSECT" -> ObjectType.INSECT;
            case "QUILL" -> ObjectType.QUILL;
            case "INKWELL" -> ObjectType.INKWELL;
            case "MANUSCRIPT" -> ObjectType.MANUSCRIPT;
            default -> null;
        };
    }

    /**
     * Converts the attributes extracted from the JSON to a usable PointStrategy object.
     *
     * @param pointStrategy            PointStrategy type. It should match one of the PointStrategy implementation.
     * @param pointStrategyType        The ObjectType needed by some of the strategies.
     * @param pointStrategyCount       The number of a given object to be counted. @see CountResource
     * @param pointStrategyLeftToRight The axis on which the strategy is going to perform the search.
     * @return The corresponding PointStrategy object, if the parameters provided are valid, null if they don't.
     * @author Lorenzo
     */
    protected PointStrategy stringsToPointStrategy(String pointStrategy, String pointStrategyType,
                                                   int pointStrategyCount, boolean pointStrategyLeftToRight) {
        switch (pointStrategy) {
            case "AllSpecial" -> {
                return new AllSpecial();
            }
            case "AnglesCovered" -> {
                return new AnglesCovered();
            }
            case "CountResource" -> {
                return new CountResource(stringToObjectType(pointStrategyType), pointStrategyCount);
            }
            case "Diagonals" -> {
                return new Diagonals(stringToObjectType(pointStrategyType), pointStrategyLeftToRight);
            }
            case "Empty" -> {
                return new Empty();
            }
            case "LConfigurationOne" -> {
                return new LConfigurationOne();
            }
            case "LConfigurationTwo" -> {
                return new LConfigurationTwo();
            }
            case "LConfigurationThree" -> {
                return new LConfigurationThree();
            }
            case "LConfigurationFour" -> {
                return new LConfigurationFour();
            }
            default -> {
                return null;
            }
        }
    }

}

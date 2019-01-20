package server.DAO;

import Types.ClassType;

import java.util.List;

/**This class is the abstract class which create the DAO for the classes
 * @author Audrey SAMSON
 */
public abstract class AbstractDAOClass {

    /**
     * Default constructor
     */
    public AbstractDAOClass() {
    }

    /**
     * @param className
     * @param descClass
     * @param idPromotion
     */
    public abstract int createClass(String className, String descClass, int idPromotion);

    /**
     * @param className
     * @param descClass
     * @param idPromotion
     */
    public abstract int updateClass(int idClass,String className, String descClass, int idPromotion);

    /**
     * @param id
     */
    public abstract int deleteClass(int id);

    public abstract List<ClassType> searchAllClasses();

    public abstract List<ClassType> searchAllClassesByPromo(int idPromo);

}

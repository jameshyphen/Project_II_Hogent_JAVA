package managers;

import domain.enums.ManagedType;
import domain.interfaces.ManagedClass;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//Whoever touches this will have to learn to live with the inevitable consequences.
final class InstanceManager {

    private static InstanceManager INSTANCE;

    /**
     * @return
     */
    public static InstanceManager get() {
        if (INSTANCE == null)
            INSTANCE = new InstanceManager();
        return INSTANCE;
    }

    private InstanceManager() {
    }


    private final Map<ManagedType, ManagedClass> instances = new HashMap<>();

    public ManagedClass getManagedInstance(ManagedType key) {
        try {
            //Ensures the instance is cached.
            ensureExists(key);

            //Retrieves the instance.
            ManagedClass instance = instances.get(key);

            return instance;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Ensures a type is in the cache by checking if it is present and creating a new instance if it isn't.
     *
     * @param key ManagedType key corresponding to the type.
     */
    private void ensureExists(ManagedType key) {
        if (!instances.containsKey(key)) {
            try {
                //Retrieves the instance and use already existing instances for dependencies where possible
                ManagedClass instance = getManagedDependency(key.classRef);

                //Adds it to the cache
                instances.put(key, instance);
            } catch (Exception e) {
                throw new IllegalArgumentException();
            }
        }
    }


    private ManagedClass getManagedDependency(Class ref) {
        var enumRef = ManagedType.fromClass(ref);//Get the enum value or null

        //Begin primary termination conditions for recursion

        if (instances.containsKey(enumRef))//Check if instance is already present
            return instances.get(enumRef);//Return value if found

        if (ref.getDeclaredConstructors().length > 1)//ManagedClass should only have 1 ctor
            return null;//more means it certainly isn't a ManagedClass or badly written, so they deserve the NullPointer

        //End primary termination conditions for recursion

        var ctor = ref.getDeclaredConstructors()[0];//Stores the single ctor

        ManagedClass returnValue = null;

        outside:
        //Used to break outside the whole block
        try {
            if (Arrays.asList(ctor.getParameterTypes())
                    .stream()
                    .anyMatch(s -> ManagedClass.class.isAssignableFrom(s))) {
                //Checks if there is another nested dependency

                var depClass =
                        Arrays
                                .stream(ctor.getParameterTypes())
                                .filter(s -> ManagedClass.class.isAssignableFrom(s))
                                .findFirst().get();
                //Gets exact subclass of the dependency by extracting the ManagedClass type

                //Begin recursion
                var dep = getManagedDependency(depClass);//Makes an instance via recursion
                //End recursion

                if (dep == null)//If none are returned cuz shit was fucked
                    break outside;//Just to return null


                returnValue = (ManagedClass) ctor.newInstance(dep);//Makes instance with dependency
            } else
                returnValue = (ManagedClass) ctor.newInstance();//If no nested ManagedClass dependencies are found

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            returnValue = null;
        }
        return returnValue;
    }

    //+1 -dzhem

}

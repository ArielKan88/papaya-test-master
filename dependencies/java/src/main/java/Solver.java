import java.util.Arrays;
import java.util.stream.Stream;
import java.util.*;

final class Solver {

    private static HashMap<String,ArrayList<String>> dependenciesMap;

    static String[] solve(String[][] dependencies) {
        return correctlySolve(dependencies);
    }

    private static String[] incorrectlySolve(String[][] dependencies) {
        return flatten(dependencies)
                .distinct()
                .toArray(String[]::new);
    }

    private static String[] correctlySolve(String[][] dependencies)
    {
        dependenciesMap = new HashMap<>();
        LinkedHashSet<String> resultSet = new LinkedHashSet<>();

        String[] tmp = flatten(dependencies).toArray(String[]::new);

        for(int i=0;i<tmp.length;i+=2)
        {
            if(!dependenciesMap.keySet().contains(tmp[i])) {
                ArrayList<String> tempList = new ArrayList<>();
                tempList.add(tmp[i + 1]);
                dependenciesMap.put(tmp[i], tempList);
            }
            else
            {
                ArrayList<String> list = dependenciesMap.get(tmp[i]);
                list.add(tmp[i+1]);
                dependenciesMap.put(tmp[i],list);
            }
        }

        for(ArrayList<String> val:dependenciesMap.values())
        {
            Iterator<String> valIterator = val.iterator();

            while (valIterator.hasNext()) {

                String currentVal = valIterator.next();
                if (!dependenciesMap.containsKey(currentVal)) {
                    resultSet.add(currentVal);
                }
                else {
                    ArrayList<String> temp = dependenciesMap.get(currentVal);

                    Iterator<String> tempIt = temp.iterator();

                    while (tempIt.hasNext()) {

                        String tempVal = tempIt.next();

                        if (!dependenciesMap.containsKey(tempVal) || resultSet.contains(tempVal)) {
                            resultSet.add(tempVal);
                            resultSet.add(currentVal);
                        }
                    }
                }
            }
        }

        for(String key:dependenciesMap.keySet())
        {
            if(!resultSet.contains(key))
            {
                resultSet.add(key);
            }
        }
        String[] correctOrder = resultSet.toArray(new String[0]);

        return correctOrder;
    }

    private static <T> Stream<T> flatten(T[][] twoDArray) {
        return Arrays
                .stream(twoDArray)
                .flatMap(Arrays::stream);
    }

}

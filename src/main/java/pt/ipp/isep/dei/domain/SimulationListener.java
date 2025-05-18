
package pt.ipp.isep.dei.domain;

import java.util.List;

@FunctionalInterface
public interface SimulationListener {

    void onTick(List<String> events);
}

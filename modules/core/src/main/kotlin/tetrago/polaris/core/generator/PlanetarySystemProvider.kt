package tetrago.polaris.core.generator

import tetrago.polaris.core.data.PlanetarySystem

interface PlanetarySystemProvider {
    /**
     * Does not store generated system.
     */
    fun generate(): PlanetarySystem
}
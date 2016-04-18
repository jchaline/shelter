package fr.jchaline.shelter.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.domain.MapCell;
import fr.jchaline.shelter.domain.MapEdge;
import fr.jchaline.shelter.domain.World;
import fr.jchaline.shelter.enums.CellEnum;

@Transactional(readOnly = true)
@Service
public class MapService {
	
	private static final int DEFAULT_WEIGHT = 1;
	
	/**
	 * Create graph with world map
	 * @param world the World map
	 * @return the graph 
	 */
	public DefaultDirectedWeightedGraph<MapCell, DefaultWeightedEdge> createGraph(World world) {
		
		DefaultDirectedWeightedGraph<MapCell, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<>(
				DefaultWeightedEdge.class);

		world.getMap().values().forEach(cell -> graph.addVertex(cell));
		
		world.getEdges().forEach(edge -> {
			DefaultWeightedEdge e = graph.addEdge(edge.getSource(), edge.getTarget());
			graph.setEdgeWeight(e, edge.getWeight());
		});

		return graph;
	}
	
	/**
	 * Find Path between two cell of the map
	 * TODO : use Cache for this method, and refresh for each new event on the map (construct, ...)
	 * Refresh cache if any of cell in path is update
	 * Warning : register in cache all sub path ([0, n], [1, n], etc ... to [n-1, n] list of cells in the path)
	 * @param world 
	 * @param source
	 * @param target
	 * @return
	 */
	public List<MapCell> computePath(World world, MapCell source, MapCell target) {
		DefaultDirectedWeightedGraph<MapCell, DefaultWeightedEdge> graph = createGraph(world);

		List<DefaultWeightedEdge> edges = DijkstraShortestPath.findPathBetween(graph, source, target);
		
		return edges.stream().map(e -> graph.getEdgeTarget(e)).collect(Collectors.toList());
	}
	
	public List<MapEdge> createEdges(World world) {
		List<MapEdge> edges = new ArrayList<>();
		for (int x = 0; x < world.getWidth(); x++) {
			for (int y = 0; y < world.getHeight(); y++) {
				MapCell cell = world.getCell(x, y);
				
				List<MapCell> neighbours = Arrays.asList(world.getCell(Math.floorMod((x + 1), world.getWidth()), y), 
														world.getCell(Math.floorMod((x - 1), world.getWidth()), y),
														world.getCell(x, Math.floorMod((y + 1), world.getHeight())),
														world.getCell(x, Math.floorMod((y - 1), world.getHeight())));
				
				neighbours.forEach(n -> {
					Optional<MapEdge> edge = createEdge(cell, n);
					edge.ifPresent(e -> edges.add(e));
				});
			}
		}
		return edges;
	}
	
	/**
	 * TODO : don't use empty occupant for green, create type of and leave empty occupant for other purpose
	 * @param from
	 * @param to
	 * @return
	 */
	public Optional<MapEdge> createEdge(MapCell from, MapCell to) {
		MapEdge edge = null;
		boolean hasRock = from.getOccupant() != null && CellEnum.ROCK.equals(from.getOccupant().getType());
		hasRock |= to.getOccupant() != null && CellEnum.ROCK.equals(to.getOccupant().getType());
		if (!hasRock) {
			int weight = DEFAULT_WEIGHT;
			if (from.getOccupant() == null && to.getOccupant() != null && CellEnum.WATER.equals(to.getOccupant().getType())) {
				weight = DEFAULT_WEIGHT * 3;
			} else if (from.getOccupant() != null && CellEnum.WATER.equals(from.getOccupant().getType()) && to.getOccupant() != null && CellEnum.WATER.equals(to.getOccupant().getType())) {
				weight = DEFAULT_WEIGHT * 5;
			} else if (from.getOccupant() != null && CellEnum.WATER.equals(from.getOccupant().getType()) && to.getOccupant() == null) {
				weight = DEFAULT_WEIGHT * 3;
			}
			edge = new MapEdge(from, to, weight);
		}

		return Optional.ofNullable(edge);
	}

}

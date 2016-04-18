package fr.jchaline.shelter.service;

import java.util.List;
import java.util.Optional;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.jchaline.shelter.dao.WorldDao;
import fr.jchaline.shelter.domain.CellOccupant;
import fr.jchaline.shelter.domain.MapCell;
import fr.jchaline.shelter.domain.MapEdge;
import fr.jchaline.shelter.domain.World;
import fr.jchaline.shelter.enums.CellEnum;

@RunWith(MockitoJUnitRunner.class)
public class MapServiceTest {

	private World WORLD_TEST;

	@Mock
	private WorldDao worldDao;

	@InjectMocks
	private MapService service = new MapService();

	@Before
	public void before() {
		WORLD_TEST = new World(30, 30);

		for (int x = 0; x < WORLD_TEST.getWidth(); x++) {
			for (int y = 0; y < WORLD_TEST.getHeight(); y++) {
				WORLD_TEST.setCell(x, y, new MapCell(x + "_" + y, x, y));
			}
		}
		
		CellOccupant rock1 = new CellOccupant("rock1", CellEnum.ROCK);
		CellOccupant rock2 = new CellOccupant("rock2", CellEnum.ROCK);
		CellOccupant rock3 = new CellOccupant("rock3", CellEnum.ROCK);
		WORLD_TEST.getCell(1, 1).setOccupant(rock1);
		WORLD_TEST.getCell(1, 2).setOccupant(rock2);
		WORLD_TEST.getCell(1, 3).setOccupant(rock3);

		CellOccupant water1 = new CellOccupant("water1", CellEnum.WATER);
		CellOccupant water2 = new CellOccupant("water2", CellEnum.WATER);
		CellOccupant water3 = new CellOccupant("water3", CellEnum.WATER);
		WORLD_TEST.getCell(3, 1).setOccupant(water1);
		WORLD_TEST.getCell(3, 2).setOccupant(water2);
		WORLD_TEST.getCell(3, 3).setOccupant(water3);
	}

	// TODO
	@Test
	public void createEdge() {
		Optional<MapEdge> createEdge = service.createEdge(WORLD_TEST.getCell(1, 1), WORLD_TEST.getCell(2, 1));
		Assert.assertFalse(createEdge.isPresent());
	}

	@Test
	public void findPath() {
		MapCell origin1 = WORLD_TEST.getCell(0, 2);
		MapCell target1 = WORLD_TEST.getCell(2, 2);

		MapCell target2 = WORLD_TEST.getCell(4, 2);
		
		WORLD_TEST.setEdges(service.createEdges(WORLD_TEST));
		DefaultDirectedWeightedGraph<MapCell, DefaultWeightedEdge> hrefGraph = service.createGraph(WORLD_TEST);

		List<DefaultWeightedEdge> findPathBetween = DijkstraShortestPath.findPathBetween(hrefGraph, origin1, target1);
		List<DefaultWeightedEdge> findPathBetween2 = DijkstraShortestPath.findPathBetween(hrefGraph, target1, target2);

		Assert.assertEquals(6, findPathBetween.size());
		Assert.assertEquals(2, findPathBetween2.size());
	}

}

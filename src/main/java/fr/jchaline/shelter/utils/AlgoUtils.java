package fr.jchaline.shelter.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

final public class AlgoUtils {
	
	/**
	 * Get random element from list
	 * @param list The list to get random element
	 * @return The element
	 */
	public static <E> E rand(List<E> list) {
		return list.get(new Random().nextInt(list.size()));
	}

	/**
	 * Remove and get random element from list
	 * @param list The list to pick random element
	 * @return The element
	 */
	public static <E> E randPick(List<E> list) {
		int nextInt = new Random().nextInt(list.size());
		return list.remove(nextInt);
	}
	
	/**
	 * Créer une représentation proportionnelle  : à partir d'une liste de valeurs "absolues",
	 * déterminer le pourcentage (entier) représentatif de chaque élément de la liste,
	 * dont le total est égal au nombre de "sièges disponibles".
	 * Dans notre cas, détermine un pourcentage par site pour un attribut.
	 * @param nseats Le nombre de places disponibles
	 * @param valeurs Les valeurs absolues
	 * @return La liste des valeurs relatives.
	 * @throws Exception en cas d'erreur lors de la répartition
	 */
	public static List<Integer> nseat(final int nseats, final List<Integer> valeurs) throws Exception {
		int somme = valeurs.stream().reduce(0, (a,b) -> {return a+b;});
		//si la somme est égal à 0, les pourcentages sont définis à 0 donc on 
		if (somme == 0) return valeurs;
		final double quota = somme / (1.0 + nseats);
		final List<Double> fractions = valeurs.stream().map(x -> x / quota).collect(Collectors.toList());

		//initialisation de la liste resultat : résulta de division "tronquée" pour ne conserver que la partie entière
		final List<Integer> resultats = fractions.stream().map(x -> x.intValue()).collect(Collectors.toList());
		
		//nomber de sièges alloués
		int sumRes = resultats.stream().reduce(0, (a,b) -> {return a+b;});

		//Nombre de sièges restants à allouer.
		int restant = nseats - sumRes;
		
		//Cas simple, pas de débordement.
		if (restant == 0) {
			return resultats;
		} else if (restant < 0) {
			//trop de places alloués
			return resultats.stream().map(x -> Math.min(x, nseats)).collect(Collectors.toList());
		} else {
			//Distribution des places restantes en fonction de l'arrondi effecué.
			final List<Double> remainders = new ArrayList<Double>();
			for (int i = 0; i < fractions.size(); i++) {
				remainders.add(fractions.get(i) - resultats.get(i));
			}
			final List<Double> remaindersCopy = new ArrayList<Double>(remainders);
			Collections.sort(remaindersCopy);
			Collections.reverse(remaindersCopy);
			final Double limit = remaindersCopy.get(restant - 1);
			
			for (int i = 0; i < remainders.size(); i++) {
				final Double remainder = remainders.get(i);
				if (remainder >= limit) {
					resultats.set(i, resultats.get(i) + 1);
					restant--;
					if (restant == 0) {
						return resultats;
					}
				}
			}
		}
		
		//TODO : trouver une solution pour ne plus avoir ce cas, et ainsi virer la gestion de l'exception dans la pile d'appels ...
		throw new Exception("Error while process nseat");
	}
	
//TODO : python version, implement with java and compare
//	def proportional(nseats, votes):
//	    quota=sum(votes)/(1.+nseats)
//	    frac=[vote/quota for vote in votes]
//	    res=[int(f) for f in frac]
//	    n=nseats-sum(res)
//	    if n==0:return res
//	    if n<0: return [min(x, nseats) for x in res]
//	    remainders=[ai-bi for ai, bi in zip(frac, res)]
//	    limit=sorted(remainders, reverse=True)[n-1]
//	    for i, r in enumerate(remainders):
//	        if r>=limit:
//	            res[i]+=1
//	            n-=1
//	            if n==0: return res
//	    raise
}

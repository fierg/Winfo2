initliste(M,N,[M|Ns]) :- M < N, M1 is M+1, initliste(M1,N,Ns).
initliste(N,N,[N]).

permutation(Xs,[Z|Zs]) :- select(Z,Xs,Ys), permutation(Ys,Zs).
permutation([],[]).

/*finde alle "Perm", die Permutationen der Liste Queens von 1 bis N sind
 * und eine Lösung zum Damenproblem liefern */
main(N) :-	
    initliste(1,N,Queens),
    permutation(Queens,Perm),
    solution(Perm),
    write(Perm).


solution(Queens) :-	%Prüfe ob alle Queens safe sind
 	safe(Queens).
 
safe([]).
 
/* die Damen sind safe, wenn für jede Dame gilt, dass sie keine Dame, die weiter rechts
 * steht angreifen kann. 
 * Überprüfe hierfür rekursiv, ob jeweils die 1., 2., 3., ... Dame safe ist,
 * indem mit noattack geprüft wird, ob sie keine der weiter rechts stehenden 
 * Damen angreifen kann.*/
safe([Queen|Others]) :- 
 safe(Others),
 noattack(Queen,Others,1).
 
noattack(_,[],_).
 
/* Eine Dame kann die "nächste" Dame mit einer Distanz von Xdist Feldern
 * genau dann nicht angreifen, wenn sie auf keiner der beiden Diagonalen steht.
 * Dies wird mit Y1 - Y =\= Xdist bzw. Y - Y1 =\= Xdist garantiert.
 * Das Überprüfen einer selben Zeile/Spalte ist unötig, da nur Permutationen 
 * von Damen auf verschiedenen Zeilen/Spalten betrachtet werden
 * -> es sind NIE 2 Damen in einer Spalte/Zeile.*/
noattack(Y,[Y1|Ylist],Xdist) :-
 Y1-Y=\=Xdist,
 Y-Y1=\=Xdist,
 Dist1 is Xdist + 1, %inkrementiere Xdist für die weiteren Spalten
 noattack(Y,Ylist,Dist1). %Prüfe rekursiv, ob auch die weiteren Damen in Ylist nicht von Y angegriffen werden



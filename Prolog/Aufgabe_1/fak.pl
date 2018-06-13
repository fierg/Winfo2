fak(0, 1).
fak(Z, F):-
    Z > 0,
    Z1 is Z - 1,
    fak(Z1, F1),
    F is Z * F1.

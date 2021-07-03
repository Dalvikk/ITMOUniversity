get_H(null, 0) :- !.
get_H(node(_, _, H, _, _, _), H).
get_L(node(_, _, _, _, L, _), L).
get_R(node(_, _, _, _, _, R), R).
get_S(null, 0).
get_S(node(_, _, _, S, _, _), S).

diff(node(_, _, _, _, L, R), D) :- get_H(L, H1), get_H(R, H2), D is H1 - H2.

max_height(H1, H2, H1) :- H1 > H2, !.
max_height(H1, H2, H2).

create_node(K, V, L, R, node(K, V, H, S, L, R)) :-
    get_H(L, H1), get_H(R, H2), max_height(H1, H2, M),
    H is M + 1,
    get_S(L, S1), get_S(R, S2),
    S is S1 + S2 + 1.

balance(P, NEW_TREE) :-
    diff(P, D), D == -2, get_R(P, R), diff(R, D2),
    (D2 =< 0 -> rotate_left(P, NEW_TREE) ; big_rotate_left(P, NEW_TREE)), !.

balance(P, NEW_TREE) :-
    diff(P, D), D == 2, get_L(P, L), diff(L, D2),
    (D2 >= 0 -> rotate_right(P, NEW_TREE) ; big_rotate_right(P, NEW_TREE)), !.

balance(P, P).

rotate_left(node(K, V, _, _, A, node(K2, V2, _, _, B, C)), NEW_TREE) :-
    create_node(K, V, A, B, Q),
    create_node(K2, V2, Q, C, NEW_TREE).

rotate_right(node(K, V, _, _, node(K2, V2, _, _, A, B), C), NEW_TREE) :-
     create_node(K, V, B, C, P),
     create_node(K2, V2, A, P, NEW_TREE).

big_rotate_left(node(K, V, _, _, L, R), NEW_TREE) :-
    rotate_right(R, C),
    create_node(K, V, L, C, A),
    rotate_left(A, NEW_TREE).

big_rotate_right(node(K, V, _, _, L, R), NEW_TREE) :-
    rotate_left(L, C),
    create_node(K, V, C, R, A),
    rotate_right(A, NEW_TREE).

map_build([], null) :- !.
map_build([(K, V) | TAIL], NEW_TREE) :- map_build(TAIL, TREE), map_put(TREE, K, V, NEW_TREE).

map_put(null, K, V, node(K, V, 1, 1, null, null)).
map_put(node(K, V, H, S, L, R), K, V2, node(K, V2, H, S, L, R)).
map_put(node(K, V, _, _, L, R), K2, V2, NEW_TREE) :-
    K2 < K,
    map_put(L, K2, V2, T),
    create_node(K, V, T, R, T2),
    balance(T2, NEW_TREE).

map_put(node(K, V, _, _, L, R), K2, V2, NEW_TREE) :-
    K2 > K,
    map_put(R, K2, V2, T),
    create_node(K, V, L, T, T2),
    balance(T2, NEW_TREE).

map_get(node(K, V, _, _, _, _), K, V).
map_get(node(K2, V2, _, _, L, _), K, V) :- K < K2, map_get(L, K, V).
map_get(node(K2, V2, _, _, _, R), K, V) :- K > K2, map_get(R, K, V).

map_remove(null, _, null).
map_remove(node(K2, V2, _, _, L, R), K, NEW_TREE) :-
    K < K2,
    map_remove(L, K, T),
    create_node(K2, V2, T, R, T2),
    balance(T2, NEW_TREE).

map_remove(node(K2, V2, _, _, L, R), K, NEW_TREE) :-
    K > K2,
    map_remove(R, K, T),
    create_node(K2, V2, L, T, T2),
    balance(T2, NEW_TREE).

map_remove(node(K, V, _, _, L, null), K, L) :- !.
map_remove(node(K, V, _, _, L, R), K, NEW_TREE) :-
    remove_min(R, T, (RK, RV)),
    create_node(RK, RV, L, T, T2),
    balance(T2, NEW_TREE).

remove_min(node(K, V, _, _, null, R), R, (K, V)) :- !.
remove_min(node(K, V, _, _, L, R), NEW_TREE, (RK, RV)) :-
    remove_min(L, T, (RK, RV)),
    create_node(K, V, T, R, T2),
    balance(T2, NEW_TREE).

map_headMapSize(null, _, 0) :- !.
map_headMapSize(node(K, V, _, _, L, _), K2, S) :- K2 =< K, map_headMapSize(L, K2, S).
map_headMapSize(node(K, V, _, _, L, R), K2, S) :-
    K2 > K, get_S(L, SL), map_headMapSize(R, K2, SR),
    S is SL + 1 + SR.

map_tailMapSize(T, K, S) :- get_S(T, S1), map_headMapSize(T, K, S2), S is S1 - S2.

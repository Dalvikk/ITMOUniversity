sieve_loop(PRIME_IDX, MIN_DIVISOR, I, N) :-
    primes(PRIME_IDX, PRIME),
    PRIME =< MIN_DIVISOR,
    NEXT_I is I * PRIME,
    NEXT_I =< N,
    assert(min_prime_divisor(NEXT_I, PRIME)),
    NEXT_PRIME_IDX is PRIME_IDX + 1,
    sieve_loop(NEXT_PRIME_IDX, MIN_DIVISOR, I, N).

build_linear_sieve(I, N, PRIMES_SZ) :-
    I =< N,
    check_prime(I, N, PRIMES_SZ, NEW_PRIMES_SZ),
    run_loop(I, N),
    NEXT_I is I + 1,
    build_linear_sieve(NEXT_I, N, NEW_PRIMES_SZ).

check_prime(I, N, PRIMES_SZ, PRIMES_SZ) :-
    min_prime_divisor(I, _), !.

check_prime(I, N, PRIMES_SZ, NEW_PRIMES_SZ) :-
    assertz(primes(PRIMES_SZ, I)),
    assertz(min_prime_divisor(I, I)),
    NEW_PRIMES_SZ is PRIMES_SZ + 1.

run_loop(I, N) :-
    min_prime_divisor(I, MIN_DIVISOR),
    not sieve_loop(1, MIN_DIVISOR, I, N).

init(MAX_N) :- build_linear_sieve(2, MAX_N, 1).
prime(N) :- min_prime_divisor(N, N).
composite(N) :- not prime(N).

prime_divisors(1, []) :- !.
prime_divisors(N, [H | T]) :-
    number(N), !,
    min_prime_divisor(N, H),
    NEXT_N is N / H,
    prime_divisors(NEXT_N, T).

prime_divisors(N, [H | T]) :-
    prod_list([H | T], N),
    prime_divisors(N, [H | T]).

prime_index(P, N) :- primes(N, P).

prod_list([H], H).
prod_list([H | T], Product) :- prod_list(T, Rest), Product is H * Rest.

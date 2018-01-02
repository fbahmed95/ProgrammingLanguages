/*******************************************/
/**    Your solution goes in this file    **/ 
/*******************************************/
fc_course(Course) :- 
    course(Course,_,Units),
    Units >= 3,
    Units =< 4.

prereq_110(Course) :-
	course(Course,Prereq,_),
	member(ecs110, Prereq).

ecs140a_students(Students) :- 
    student(Students, Class),
    member(ecs140a, Class).
    
% Find the names of all instructors who 
% teach john’s courses (instructornames).
instructor_names(Instructor) :- 
    instructor(Instructor, IClass),
    student(john, SClass), 
    instructornamesets(IClass, SClass). 

instructornamesets(IClass, SClass) :- 
    nth(_, IClass, Common), 
    nth(_, SClass, Common), !.

% Find the names of all students who are 
% in jim’s class (students).
students(STUDENTS) :- 
    instructor(jim, IClass),
    student(STUDENTS, SClass), 
    studentssets(IClass, SClass). 

studentssets(IClass, SClass) :- 
    nth(_, IClass, Common),
    nth(_, SClass, Common), !.

% Find all pre-requisites of a course (allprereq). This 
% will involve finding not only the immediate prerequisites of 
% a course, but pre-requisites and so on. 
is_prereq(Class, Prereq) :- 
    course(Class, PreReqList, _), 
    member(Prereq, PreReqList).

is_bigger_prereq(Class, BiggerPrereq) :- 
    is_prereq(Class, BiggerPrereq). 
is_bigger_prereq(Class, BiggerPrereq) :- 
    is_prereq(Class, MiddlePrereq), 
    is_bigger_prereq(MiddlePrereq, BiggerPrereq).
    
allprereq(Class, All_pre) :-
    findall(X, is_bigger_prereq(Class, X), Y),
    reverse(Y, All_pre). 


% Write a predicate,alllength, that takes a list and counts the 
% number of atoms that occur in the list at all levels.
all_length([], 0) :- 
    !. 
all_length([H | T], X) :-
    H \= [],
    all_length(H, HY),
    all_length(T, TY), 
    X is HY + TY, !.
all_length([H | T], X) :-
    H = [],
    all_length(T, Count),
    X is 1 + Count, !.
all_length(Inp, X) :-
    atomic(Inp), 
    X is 1.

% Write a predicate equalab(L), which returns true if L contains
% an equal number of a and b terms

%COUNTING NUMBER OF a'S
count_a([], 0) :- 
    !. 
count_a(Inp, X) :-
    atomic(Inp), 
    Inp = a, 
    X is 1. 
count_a(Inp, X) :-
    atomic(Inp), 
    Inp \= a, 
    X is 0. 
count_a([H | T], X) :- 
    H \= a,
    count_a(H, HX), 
    count_a(T, TX), 
    X is HX + TX,
    !. 
count_a([H | T], X) :- 
    H = a,
    count_a(H, HX), 
    count_a(T, TX), 
    X is HX + TX,
    !.
%COUNTING NUMBER OF b'S
count_b([], 0) :- 
    !. 
count_b(Inp, X) :-
    atomic(Inp), 
    Inp = b, 
    X is 1. 
count_b(Inp, X) :-
    atomic(Inp), 
    Inp \= b, 
    X is 0. 
count_b([H | T], X) :- 
    H \= b,
    count_b(H, HX), 
    count_b(T, TX), 
    X is HX + TX,
    !. 
count_b([H | T], X) :- 
    H = b,
    count_b(H, HX), 
    count_b(T, TX), 
    X is HX + TX,
    !.

%CHECKING IF EQUAL   
equal_a_b([], 0) :- 
    !.
equal_a_b(L):- 
    count_a(L, Acount),
    count_b(L, Bcount),
    Acount = Bcount.
    
%Define a predicate,swap_prefix_suffix, such that:
% a. K is a sublist of L, 
% b. S is a list obtained by taking whatever is after K
%    in L, plus K, plus whatever was before K.
swap_prefix_suffix(K, L, S).

% Define a predicate,palin(A)that is true if the list A is a palindrome, 
% that is, it reads the same backwardsas forwards. For instance,
% [1, 2, 3, 2, 1]is a palindrome, but[1, 2]is not.
palin(L) :-
    reverse(L,L).

% A  good  sequence  consists  either  of  the  single  number  0,  or  
% of  the  number  1  followed  by  two  othergood sequences:  thus,
% [1,0,1,0,0] is a good sequence, but [1,1,0,0]is not.  Define a 
% relationgood(A)that is true ifAis a good sequence.

good([0]) :- !.
good(Inp) :-
    atomic(Inp), 
    Inp is 0, !. 
good([H|[HT | T]]) :-
    H is 1, 
    good(HT),
    good(T), !.
good([H|T]) :-
    good(H), 
    good(T), !.
        
%  A farmer must ferry a wolf, a goat, and a cabbage across a river using 
%  a boat that is too small to take more than one of the three across at once.
state(_,_,_,_).
    
opposite(left, right).
opposite(right, left).

unsafe(state(F,W,G,_)):-
    W = G, F \= G.
unsafe(state(F,_,G,C)):-
    G = C, F \= G.

safe(S):-
    \+ unsafe(S).

take(X,A,B):- opposite(A,B).
arc(take(wolf,X,Y), state(X,X,G,C), state(Y,Y,G,C)):-      % Take wolf
    opposite(X, Y),
    safe(state(Y,Y,G,C)).

arc(take(goat,X,Y), state(X,W,X,C), state(Y,W,Y,C)):-      % Take goat
    opposite(X, Y),
    safe(state(Y,W,Y,C)).
    
arc(take(cabbage,X,Y), state(X,W,G,X), state(Y,W,G,Y)):-      % Take cabbage
    opposite(X, Y),
    safe(state(Y,W,G,Y)).
    
arc(take(none,X,Y), state(X,W,G,C), state(Y,W,G,C)):-      % Take farmer himself
    opposite(X, Y),
    safe(state(Y,W,G,C)).

print_([]).
print_(T):-
    atomic(T),
    write(T).
print_([H|T]):-
    write(H), nl,
    print_(T).

go(S1, S2, Visited, TAKELIST):-
    arc(TAKE, S1, S2),
    \+ member(S1, Visited),
    reverse([TAKE|TAKELIST], A),
    print_(A).

go(S1, S2, Visited, TAKELIST):-
    arc(TAKE, S1, SX),   % take anyone which has safe path
    \+ member(S1, Visited),
    go(SX, S2, [S1 | Visited], [TAKE | TAKELIST]), !.
   
solve:-
    go(state(left, left, left, left), state(right, right, right, right), [], []).

    

maennlich(knut).
maennlich(franz).
maennlich(tim).
maennlich(otto).
maennlich(bob).
maennlich(peter).
maennlich(anton).

weiblich(antonia).
weiblich(inge).
weiblich(anna).
weiblich(uschi).
weiblich(ute).
weiblich(maria).

vater_von(knut, inge).
vater_von(knut, anna).
vater_von(franz, tim).
vater_von(otto, uschi).
vater_von(otto, bob).
vater_von(peter, ute).
vater_von(bob, anton).

mutter_von(antonia, inge).
mutter_von(antonia, anna).
mutter_von(inge, tim).
mutter_von(anna, uschi).
mutter_von(anna, bob).
mutter_von(uschi, ute).
mutter_von(maria, anton).

partner_von(Mutter, Vater) :- mutter_von(Mutter, Kind), vater_von(Vater, Kind).
partner_von(Vater, Mutter) :- mutter_von(Mutter, Kind), vater_von(Vater, Kind).

schwester_von(Schwester, Person) :- vater_von(Vater, Schwester), vater_von(Vater, Person), weiblich(Schwester), Schwester\=Person.
bruder_von(Bruder, Person) :- vater_von(Vater, Bruder), vater_von(Vater, Person), maennlich(Bruder), Bruder\=Person.

tante_von(Tante, Person) :- schwester_von(Tante, Mutter), mutter_von(Mutter, Person).
tante_von(Tante, Person) :- schwester_von(Tante, Vater), vater_von(Vater, Person).
tante_von(Tante, Person) :- partner_von(Tante, Onkel), bruder_von(Onkel, Mutter), mutter_von(Mutter, Person).
tante_von(Tante, Person) :- partner_von(Tante, Onkel), bruder_von(Onkel, Vater), vater_von(Vater, Person).
onkel_von(Onkel, Person) :- partner_von(Onkel, Tante), tante_von(Tante, Person).

grossmutter_von(Oma, Enkelkind) :- mutter_von(Oma, Mutter), mutter_von(Mutter, Enkelkind).
grossmutter_von(Oma, Enkelkind) :- mutter_von(Oma, Vater), vater_von(Vater, Enkelkind).
grossvater_von(Opa, Enkelkind) :- partner_von(Opa, Oma), grossmutter_von(Oma, Enkelkind).

cousine_von(Cousine, Person) :- weiblich(Cousine), grossmutter_von(Oma, Cousine), grossmutter_von(Oma, Person), Cousine\=Person, \+ schwester_von(Cousine, Person).
cousin_von(Cousin, Person) :- maennlich(Cousin), grossmutter_von(Oma, Cousin), grossmutter_von(Oma, Person), Cousin\=Person, \+ bruder_von(Cousin, Person).

vorfahr_von(Vorfahr, Person) :- mutter_von(Vorfahr, Person).
vorfahr_von(Vorfahr, Person) :- vater_von(Vorfahr, Person).
vorfahr_von(Vorfahr, Person) :- mutter_von(Vorfahr, Kind), vorfahr_von(Kind, Person).
vorfahr_von(Vorfahr, Person) :- vater_von(Vorfahr, Kind), vorfahr_von(Kind, Person).

einzelkind(Kind) :- maennlich(Kind), \+ bruder_von(Kind, _).
einzelkind(Kind) :- weiblich(Kind), \+ schwester_von(Kind, _).

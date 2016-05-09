progenitor(maria,jose).
progenitor(joao,jose).
progenitor(maria,marcos).
progenitor(joao,marcos).
progenitor(marcos, rute).
progenitor(joao,ana).
progenitor(jose,julia).
progenitor(jose,iris).
progenitor(iris,jorge).
irmaos(jose,ana).
irmaos(julia,iris).
% http://www.swi-prolog.org/pldoc/man?section=pldoc-comments


% perguntas relevantes
% Quem sao os filhos de maria?
% ?- progenitor(maria, X)

% Quem sao os pais de maria?
% ?- progenitor(X,maria)

% Quem e fiho de quem?
% ?- progenitor(X,Y)

% pergutas complexas: Quem sao os avos de jorge
% Como nao temos a rela��o avo(X,Y) dividimos a pergunta em 2
%
% 1) quem sao os progenitores de jorge
% 2) quem sao os progenitores dos progenitores de jorge
%
% Assim:
% Encontre X e Y, tal que X seja progenitor de Y e Y progenitor de jorge
% progenitor(X, Y), progenitor(Y, jorge).
%
% podemos inverter a pergunta para
%
% Encontre Y que seja progenitor de jorge,tal que X seja progenitor de Y
% progenitor(Y, jorge), progenitor(X, Y).
%
% Encontre os netos de JOAO
%
% 1) encontre os filhos de joao (X)
% 2) encontre os filhos de (X)
% progenitor(joao, X), progenitor(X, Y).
%
% Jose e Ana possuem o progenitor em comum ?
% Encontre um X tal que X seja progenitor de jose e ana simultanemente
% progenitor(X, jose), progenitor(X, ana)
%
% Encontre os filhos de maria e joao
% progenitor(maria, Y), progenitor(joao, Y)
%
% Encontre os netos de maria e jose
%
% progenitor(X,Y), progenitor(maria,Y), progenitor(joao, Y).
%
% ESSA CONSULTA VOLTA OS NETOS em X e os pais em Y, onde Y sao os filhos
% de maria e joao
% progenitor(maria,Y), progenitor(joao, Y), progenitor(Y,X).
%
% Tio dos filhos de jose, os seus irmaos
%
% progenitor(X, jose), progenitor(X, Y)
%
%

% declarando uma relacao inversa ao progenitor
%

feminino(ana).
feminino(maria).
masculino(joao).

% clausulas do tipo abaixo sao denominadas regras
filho(Y, X) :- progenitor(X, Y).
filha(Y, X) :- progenitor(X, Y), feminino(Y).

% regra mae
mae(X, Y) :- progenitor(X,Y), feminino(X).

% regra avo
avo(X, Z) :- progenitor(X, Y), progenitor(Y, Z).
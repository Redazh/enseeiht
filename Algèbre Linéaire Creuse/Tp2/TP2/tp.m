close all;
clear all;

eps = 1e-16;

% c'est vide et c'est normal
%load ("mat0.mat");
%load ("mat1.mat");
load ("mat2.mat");
%load ("mat3.mat");
%load ("bcsstk27.mat");

nb_opListe = zeros(1,7);
erreurListe = zeros(1,7);


%verification sym et definie positive
if (issymmetric(A) == 1)
  fprintf('La matrice A est symetrique \n')
end

if ((all(eig(A)>0)) == 1)
  fprintf('La matrice A est definie positive \n')
end

b = [1:length(A)]';
%Resoudre sys Ax=b avec chol
R = chol(A);
L = R';
y = L\b;
x=L'\y;

n=length(b);
nb_oper=2*nb_operation(n,nnz(L));

if (normwise(A,b,x) > eps)
    fprintf('pas de convergence \n')
end

fprintf('Nombre d opération pour le cas sans permutation est : %d\n',nb_oper)

%Permutation amd
P1 = amd(A);

C1 = A(P1,P1);

R1=chol(C1);
L1 = R1';


y1 = L1\(b(P1));
x1 = (L1)'\y1;

n=length(b);
nb_oper1=2*nb_operation(n,nnz(L1));

%Depermuter x1
[~,P12] = sort(P1);
if (normwise(A,b,x1(P12)) > eps)
    fprintf('pas de convergence avec amd \n')
end

Methodes = ["Amd"];
nb_opListe(1) = nb_oper1;
erreurListe(1) = normwise(A,b,x1(P12));

fprintf('Nombre d opération avec une permutation amd est : %d\n',nb_oper1)


%Permutation colamd
P2 = colamd(A);

C2 = A(P2,P2);

R2=chol(C2);
L2 = R2';

y2 = L2\(b(P2));
x2 = (L2)'\y2;

n=length(b);
nb_oper2=2*nb_operation(n,nnz(L2));

[~,P22] = sort(P2);
if (normwise(A,b,x2(P22))> eps)
    fprintf('pas de convergence avec colamd\n')
end

Methodes(2) = "Colamd";
nb_opListe(2) = nb_oper2;
erreurListe(2) = normwise(A,b,x2(P22));

fprintf('Nombre d opération avec une permutation colamd est : %d\n',nb_oper2)

%Permutation symamd
P3 = symamd(A);

C3 = A(P3,P3);

R3=chol(C3);
L3 = R3';

y3 = L3\(b(P3));
x3 = (L3)'\y3;

n=length(b);
nb_oper3=2*nb_operation(n,nnz(L3));

[~,P32] = sort(P3);
if (normwise(A,b,x3(P32))> eps)
    fprintf('pas de convergence avec symamd \n')
end

Methodes(3) = "Symamd";
nb_opListe(3) = nb_oper3;
erreurListe(3) = normwise(A,b,x3(P32));

fprintf('Nombre d opération avec une permutation symamd est : %d\n',nb_oper3)

%Permutation symrcm
P4 = symrcm(A);

C4 = A(P4,P4);

R4=chol(C4);
L4 = R4';

y4 = L4\(b(P4));
x4 = (L4)'\y4;

n=length(b);
nb_oper4=2*nb_operation(n,nnz(L4));

[~,P42] = sort(P4);
if (normwise(A,b,x4(P42))> eps)
    fprintf('pas de convergence avec symrcm \n')
end

Methodes(4) = "Symrcm";
nb_opListe(4) = nb_oper4;
erreurListe(4) = normwise(A,b,x4(P42));

fprintf('Nombre d opération avec une permutation symrcm est : %d\n',nb_oper4)

%Permutation colperm
P5 = colperm(A);
C5 = A(P5,P5);

R5=chol(C5);
L5 = R5';

y5 = L5\(b(P5));
x5 = (L5)'\y5;
n=length(b);
nb_oper5=2*nb_operation(n,nnz(L5));
[~,P52] = sort(P5);
if (normwise(A,b,x5(P52))>eps)
 fprintf('pas de convergence avec colperm \n');
end

Methodes(5) = "Colperm";
nb_opListe(5) = nb_oper5;
erreurListe(5) = normwise(A,b,x5(P52));

fprintf('Nombre d opération avec une permutation colperm est : %d\n',nb_oper5)

%Permutation dmperm 
P6 = dmperm(A);
C6 = A(P6,P6);
R6=chol(C6);
L6 = R6';
y6 = L6\(b(P6));
x6 = (L6)'\y6;
n=length(b);
nb_oper6=2*nb_operation(n,nnz(L6));
[~,P62] = sort(P6);
if (normwise(A,b,x6(P62))>eps)
 fprintf('pas de convergence avec dmperm \n');
end

Methodes(6) = "Dmperm";
nb_opListe(6) = nb_oper6;
erreurListe(6) = normwise(A,b,x6(P62));

fprintf('Nombre d opération avec une permutation dmperm est : %d\n',nb_oper6)

%Permutation dissect 
P7 = dissect(A);
C7 = A(P7,P7);
R7=chol(C7);
L7 = R7';
y7 = L7\(b(P7));
x7 = (L7)'\y7;
n=length(b);
nb_oper7=2*nb_operation(n,nnz(L7));
[~,P72] = sort(P7);
if (normwise(A,b,x7(P72))>eps)
 fprintf('pas de convergence avec dissect \n');
end

Methodes(7) = "Dissect";
nb_opListe(7) = nb_oper7;
erreurListe(7) = normwise(A,b,x7(P72));

fprintf('Nombre d opération avec une permutation dissect est : %d\n',nb_oper7)

%% indiquer la meilleur permutation
[nbmin,i] = min(nb_opListe);
[erreurMin,j] = min(erreurListe);
fprintf('La meilleur permutation au sens du nombre de flops est: %s \n',Methodes(i))
fprintf('La meilleur permutation au sens de la qualité de la solution est: %s \n',Methodes(j))

%% affichage de A

subplot(3,3,1)
spy(A)
title('Sans permutation')

subplot(3,3,2)
spy(C1)
title('amd')

subplot(3,3,3)
spy(C2)
title('colamd')

subplot(3,3,4)
spy(C3)
title('symamd')

subplot(3,3,5)
spy(C4)
title('symrcm')

subplot(3,3,6)
spy(C5)
title('colperm')

subplot(3,3,7)
spy(C6)
title('dmperm')

subplot(3,3,8)
spy(C7)
title('dissect')

sgtitle('Affichage de A après permutation')

%% affichage de L
figure(2)
subplot(3,3,1)
spy(L)
title('Sans permutation')

subplot(3,3,2)
spy(L1)
title('amd')

subplot(3,3,3)
spy(L2)
title('colamd')

subplot(3,3,4)
spy(L3)
title('symamd')

subplot(3,3,5)
spy(L4)
title('symrcm')

subplot(3,3,6)
spy(L5)
title('colperm')

subplot(3,3,7)
spy(L6)
title('dmperm')

subplot(3,3,8)
spy(L7)
title('dissect')

sgtitle('Affichage de L après permutation')










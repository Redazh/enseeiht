close all;
clear all;

eps = 1e-15;

% c'est vide et c'est normal
load ("hydcar20.mat");
%load ("pde225_5e-1.mat");
%load ("piston.mat");


nb_opListe = zeros(1,7);
erreurListe = zeros(1,7);

B= [1:length(A)]';
%Resoudre sys Ax=b avec une factorisation LU
[L,U,P] = lu(A);
y = L\(P*B);
x=U\y;

n=length(B);
nb_oper=nb_operation(n,nnz(L))+nb_operation(n,nnz(U));

if (normwise(A,B,x) > eps)
    fprintf('pas de convergence \n')
end


fprintf('Nombre d opération pour le cas sans permutation est : %d\n',nb_oper)

%Permutation amd
P1 = amd(A);

C1 = A(P1,P1);

[L1,U1,P13] = lu(C1);

y1 = L1\(P13*B(P1));
x1 = U1\y1;

nb_oper1=nb_operation(n,nnz(L1))+nb_operation(n,nnz(U1));


[~,P12] = sort(P1);

if (normwise(A,B,x1(P12)) > eps)
    fprintf('pas de convergence avec amd \n')
end

Methodes = ["Amd"];
nb_opListe(1) = nb_oper1;
erreurListe(1) = normwise(A,B,x1(P12));

fprintf('Nombre d opération avec une permutation amd est : %d\n',nb_oper1)


%Permutation colamd
P2 = colamd(A);

C2 = A(P2,P2);

[L2,U2,P23] = lu(C2);
y2 = L2\(P23*B(P2));
x2 = U2\y2;

nb_oper2=nb_operation(n,nnz(L2))+nb_operation(n,nnz(U2));

[~,P22] = sort(P2);
if (normwise(A,B,x2(P22)) > eps)
    fprintf('pas de convergence avec colamd \n')
end

Methodes(2) = "Colamd";
nb_opListe(2) = nb_oper2;
erreurListe(2) = normwise(A,B,x2(P22));

fprintf('Nombre d opération avec une permutation colamd est : %d\n',nb_oper2)

%Permutation symamd
P3 = symamd(A);

C3 = A(P3,P3);

[L3,U3,P33] = lu(C3);
y3 = L3\(P33*B(P3));
x3 = U3\y3;

nb_oper3=nb_operation(n,nnz(L3))+nb_operation(n,nnz(U3));

[~,P32] = sort(P3);
if (normwise(A,B,x3(P32)) > eps)
    fprintf('pas de convergence avec symamd \n')
end

Methodes(3) = "Symamd";
nb_opListe(3) = nb_oper3;
erreurListe(3) = normwise(A,B,x3(P32));

fprintf('Nombre d opération avec une permutation symamd est : %d\n',nb_oper3)

%Permutation symrcm
P4 = symrcm(A);

C4 = A(P4,P4);

[L4,U4,P43] = lu(C4);
y4 = L4\(P43*B(P4));
x4 = U4\y4;

nb_oper4=nb_operation(n,nnz(L4))+nb_operation(n,nnz(U4));

[~,P42] = sort(P4);
if (normwise(A,B,x4(P42)) > eps)
    fprintf('pas de convergence avec symrcm \n')
end

Methodes(4) = "Symrcm";
nb_opListe(4) = nb_oper4;
erreurListe(4) = normwise(A,B,x4(P42));

fprintf('Nombre d opération avec une permutation symrcm est : %d\n',nb_oper4)

%Permutation colperm
P5 = colperm(A);
C5 = A(P5,P5);

[L5,U5,P53] = lu(C5);
y5 = L5\(P53*B(P5));
x5 = U5\y5;

nb_oper5=nb_operation(n,nnz(L5))+nb_operation(n,nnz(U5));

[~,P52] = sort(P5);
if (normwise(A,B,x5(P52)) > eps)
    fprintf('pas de convergence avec colperm \n')
end

Methodes(5) = "Colperm";
nb_opListe(5) = nb_oper5;
erreurListe(5) = normwise(A,B,x5(P52));

fprintf('Nombre d opération avec une permutation colperm est : %d\n',nb_oper5)

%Permutation dmperm 
P6 = dmperm(A);
C6 = A(P6,P6);

[L6,U6,P63] = lu(C6);
y6 = L6\(P63*B(P6));
x6 = U6\y6;

nb_oper6=nb_operation(n,nnz(L6))+nb_operation(n,nnz(U6));

[~,P62] = sort(P6);
if (normwise(A,B,x6(P62)) > eps)
    fprintf('pas de convergence avec dmperm \n')
end

Methodes(6) = "Dmperm";
nb_opListe(6) = nb_oper6;
erreurListe(6) = normwise(A,B,x6(P62));

fprintf('Nombre d opération avec une permutation dmperm est : %d\n',nb_oper6)

%Permutation dissect 
P7 = dissect(A);
C7 = A(P7,P7);

[L7,U7,P73] = lu(C7);
y7 = L7\(P73*B(P7));
x7 = U7\y7;

nb_oper7=nb_operation(n,nnz(L7))+nb_operation(n,nnz(U7));

[~,P72] = sort(P7);
if (normwise(A,B,x7(P72)) > eps)
    fprintf('pas de convergence avec dissect \n')
end

Methodes(7) = "Dissect";
nb_opListe(7) = nb_oper7;
erreurListe(7) = normwise(A,B,x7(P72));

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

%% affichage de U
figure(3)
subplot(3,3,1)
spy(U)
title('Sans permutation')

subplot(3,3,2)
spy(U1)
title('amd')

subplot(3,3,3)
spy(U2)
title('colamd')

subplot(3,3,4)
spy(U3)
title('symamd')

subplot(3,3,5)
spy(U4)
title('symrcm')

subplot(3,3,6)
spy(U5)
title('colperm')

subplot(3,3,7)
spy(U6)
title('dmperm')

subplot(3,3,8)
spy(U7)
title('dissect')

sgtitle('Affichage de U après permutation')
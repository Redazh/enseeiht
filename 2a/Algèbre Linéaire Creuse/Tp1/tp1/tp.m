close all;
clear all;


%load pde225_5e-1;
load hydcar20.mat; 
%load mat1;
n = size(A,1);
fprintf('dimension de A : %4d \n' , n);

b = [1:n]';

x0 = zeros(n, 1);

eps = 1e-9;

kmax = n;

fprintf('FOM\n');
[x, flag, relres, iter, resvec] = krylov(A, b, x0, eps, kmax, 0);
fprintf('Nb iterations FOM : %4d \n' , iter);
semilogy(resvec, 'c');
if(flag == 0)
  fprintf('pas de convergence\n');
end


pause

fprintf('GMRES\n');
[x, flag, relres, iter, resvec] = krylov(A, b, x0, eps, kmax, 1);
fprintf('Nb iterations gmres : %4d \n' , iter);
hold on
semilogy(resvec, 'r');
if(flag == 0)
  fprintf('pas de convergence\n');
end

pause 

M1 = eye(length(A));

M2 = eye(length(A));

fprintf('GMRES_matlab\n');
[x, flag, relres, iter, resvec] = gmres(A, b,[],eps,kmax,M1,M2,x0);

fprintf('Nb iterations : %4d \n' , iter(2));
hold on
semilogy(resvec, 'b');
if(flag ~= 0)
  fprintf('pas de convergence\n');
end

legend('FOM', 'GMRES', 'GMRES\_Matlab');
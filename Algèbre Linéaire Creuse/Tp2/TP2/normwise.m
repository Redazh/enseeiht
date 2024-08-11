function normw = normwise(A,b,x)
    normw = (norm(b-full(A)*x))/((norm(full(A)))*(norm(x))+(norm(b)));
end




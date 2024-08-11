using LinearAlgebra
"""
Approximation de la solution du problème 

    min qₖ(s) = s'gₖ + 1/2 s' Hₖ s, sous la contrainte ‖s‖ ≤ Δₖ

# Syntaxe

    s = gct(g, H, Δ; kwargs...)

# Entrées

    - g : (Vector{<:Real}) le vecteur gₖ
    - H : (Matrix{<:Real}) la matrice Hₖ
    - Δ : (Real) le scalaire Δₖ
    - kwargs  : les options sous formes d'arguments "keywords", c'est-à-dire des arguments nommés
        • max_iter : le nombre maximal d'iterations (optionnel, par défaut 100)
        • tol_abs  : la tolérence absolue (optionnel, par défaut 1e-10)
        • tol_rel  : la tolérence relative (optionnel, par défaut 1e-8)

# Sorties

    - s : (Vector{<:Real}) une approximation de la solution du problème

# Exemple d'appel

    g = [0; 0]
    H = [7 0 ; 0 2]
    Δ = 1
    s = gct(g, H, Δ)

"""
function gct(g::Vector{<:Real}, H::Matrix{<:Real}, Δ::Real; 
    max_iter::Integer = 100, 
    tol_abs::Real = 1e-10, 
    tol_rel::Real = 1e-8)
    
    function racine(s,p,Δ)
        a = norm(p)^2
        b = 2*s'*p
        c = norm(s)^2 - Δ^2
        delta = b^2 - 4*a*c
        if delta == 0
            r = -b/(2*a)
        elseif delta > 0
            r1 = (-b-sqrt(delta))/(2*a)
            r2 = (-b+sqrt(delta))/(2*a)
            qr1 = g'*(s+r1*p) + (1/2)*(s+r1*p)'*H*(s+r1*p)
            qr2 = g'*(s+r2*p) + (1/2)*(s+r2*p)'*H*(s+r2*p)
            if qr1 < qr2
                r = r1
            else 
                r = r2
            end
        end
        return r
    end
    
    function racinepositif(s,p,Δ)
        a = norm(p)^2
        b = 2*s'*p
        c = norm(s)^2 - Δ^2
        delta = b^2 - 4*a*c
        if delta == 0
            r = -b/(2*a)
        elseif delta > 0
            r1 = (-b-sqrt(delta))/(2*a)
            r2 = (-b+sqrt(delta))/(2*a)
            if 0 < r1
                r = r1
            else 
                r = r2
            end
        end
        return r
    end

    j = 0
    gj = g 
    pj = (-g)
    sj = zeros(length(g))
    
    
    while (j<=max_iter) && (norm(gj)>max(norm(g)*tol_rel,tol_abs))
        Kj = pj' * H * pj
        if Kj <= 0
           rhoj = racine(sj,pj,Δ)
           return sj+rhoj*pj
        end
        alphaj = gj'*gj/Kj
        if norm(sj+alphaj*pj) >= Δ
           rhoj = racinepositif(sj,pj,Δ)
           return sj+rhoj*pj
        end
        sj = sj + alphaj*pj
        gj_1 = gj  #gj
        gj = gj +alphaj*H*pj
        betaj = (gj'*gj)/(gj_1'*gj_1)
        pj = -gj + betaj*pj
        j = j+1
    end

   return sj
end

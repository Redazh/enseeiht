# Ecrire les tests de l'algorithme du pas de Cauchy
using Test

function tester_cauchy(cauchy::Function)

	@testset "Pas de Cauchy test1" begin
        g = [0; 0]
        H = [7 0; 0 2]
        Δ = 1
        s = cauchy(g, H, Δ)
        @test s == [0.0;0.0]

        g = [1; 1]
        H = [-7 0; 0 1]
        Δ = 1
        s = cauchy(g, H, Δ)
        @test s == -(Δ/norm(g))*g
        
        
        g = [1; 1]
        H = [-7 0; 0 -1]
        Δ = 1
        s = cauchy(g, H, Δ)
        @test s == -(Δ/norm(g))*g
        
        g = [1; 1]
        H = [2 0; 0 1]
        Δ = 1
        s = cauchy(g, H, Δ)
        @test s ≈ -(2/3)*g atol=1e-1
    end

end
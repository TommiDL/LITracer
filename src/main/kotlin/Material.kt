package org.example

/**
 * Class representing a material in a rendering scene.
 * @emitted_radiance = the pigment representing the emitted radiance of the material (default is white color)
 * @brdf = the bidirectional reflectance distribution function (BRDF) of the material (default is a diffusive BRDF using the emitted radiance)
 */
class Material (
    val emitted_radiance:Pigment=UniformPigment(Color(255f,255f,255f)),
    val brdf: BRDF=DiffusiveBRDF(emitted_radiance))
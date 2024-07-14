package org.example

enum class KeywordEnum(val Enum: Int) {
    /**
     * Enumeration for all the possible keywords recognized by the lexer
     */
    NEW(1),
    MATERIAL(2),
    PLANE(3),
    SPHERE(4),
    DIFFUSE(5),
    SPECULAR(6),
    UNIFORM(7),
    CHECKERED(8),
    IMAGE(9),
    IDENTITY(10),
    TRANSLATION(11),
    ROTATION_X(12),
    ROTATION_Y(13),
    ROTATION_Z(14),
    SCALING(15),
    CAMERA(16),
    ORTHOGONAL(17),
    PERSPECTIVE(18),
    FLOAT(19),
    POINT_LIGHT(20),
    TRIANGLE(21),
    MESH(21),
}

val KEYWORDS: Map<String, KeywordEnum> = mapOf(
    "new" to KeywordEnum.NEW,
    "material" to KeywordEnum.MATERIAL,
    "plane" to KeywordEnum.PLANE,
    "sphere" to KeywordEnum.SPHERE,
    "diffuse" to KeywordEnum.DIFFUSE,
    "specular" to KeywordEnum.SPECULAR,
    "uniform" to KeywordEnum.UNIFORM,
    "checkered" to KeywordEnum.CHECKERED,
    "image" to KeywordEnum.IMAGE,
    "identity" to KeywordEnum.IDENTITY,
    "translation" to KeywordEnum.TRANSLATION,
    "rotation_x" to KeywordEnum.ROTATION_X,
    "rotation_y" to KeywordEnum.ROTATION_Y,
    "rotation_z" to KeywordEnum.ROTATION_Z,
    "scaling" to KeywordEnum.SCALING,
    "camera" to KeywordEnum.CAMERA,
    "orthogonal" to KeywordEnum.ORTHOGONAL,
    "perspective" to KeywordEnum.PERSPECTIVE,
    "float" to KeywordEnum.FLOAT,
    "point_light" to KeywordEnum.POINT_LIGHT,
    "triangle" to KeywordEnum.TRIANGLE,
    "mesh" to KeywordEnum.MESH
)


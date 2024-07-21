
import org.example.*
import kotlin.math.abs

class Cube(
    override val transformation: Transformation=Transformation(),
    override val material: Material= Material()
):Shape()
{


    val xmin:Float=-1f
    val xmax:Float=1f


    override fun ray_intersection(ray: Ray): HitRecord? {
        val inv_ray=ray.transform(this.transformation.inverse())

        val dir:Vec=inv_ray.dir
        val origin:Point=inv_ray.origin





        // x intersection
        val txmin =(xmin-origin.x)/dir.x
        val txmax = (xmax-origin.x)/dir.x

        // y intersection
        val tymin = (xmin-origin.y)/dir.y
        val tymax = (xmax-origin.y)/dir.y

        // x intersection
        val tzmin = (xmin-origin.z)/dir.z
        val tzmax = (xmax-origin.z)/dir.z


        var xintersection:Boolean=true
        var yintersection:Boolean=true
        var zintersection:Boolean=true

        // just need one of them to be finite
        if ((txmin.isInfinite()) and (txmax.isInfinite()))
        {
            xintersection=false
        }

        if ((tymin.isInfinite()) and (tymax.isInfinite()))
        {
            yintersection=false
        }

        if ((tzmin.isInfinite()) and (tzmax.isInfinite()))
        {
            zintersection=false
        }

        if(!xintersection and !yintersection and !zintersection) {
            return null
        }


        val xy:Boolean=if (xintersection and yintersection) interval_intersection(txmin, txmax, tymin, tymax) else true
        val xz:Boolean=if (xintersection and zintersection) interval_intersection(txmin, txmax, tzmin, tzmax) else true
        val yz:Boolean=if (yintersection and zintersection) interval_intersection(tymin, tymax, tzmin, tzmax) else true


        if (
            ( (xy) or (xz) or (yz) ) and (xintersection or yintersection or zintersection) //no intersection at all
        )
        {

            val t= interval_intersection(txmin, txmax, tymin, tymax, tzmin, tzmax)

            if(t==null) return null
            if(t.first.isInfinite()) return null

//            println(t.first)
  //          println(t.second)

            val hit_point:Point=inv_ray.at(t.first)
            //println(hit_point)

            if( (hit_point.x>1f) or (hit_point.y>1f) or (hit_point.z>1f) or (hit_point.x<-1f) or (hit_point.y<-1f) or (hit_point.z<-1f)  )
                return null

            return HitRecord(
                world_point = this.transformation*hit_point,
                normal = this.transformation*_cube_normal(hit_point, ray.dir),
                surface_point = cube_point_to_uv(hit_point),
                t=t.first,
                ray=ray,
                material=this.material
            )

        }


        return null
    }

    fun _cube_normal(point: Point, ray_dir:Vec):Normal
    {
        //xmax face
        if(point.x==xmax)
        {
            return Normal(1f, 0f,0f)
        }
        // xmin face
        else if(point.x==xmin)
        {
            return Normal(-1f, 0f,0f)
        }
        // ymax face
        else if(point.y==xmax)
        {
            return Normal(0f, 1f,0f)
        }
        // ymin face
        else if(point.y==xmin)
        {
            return Normal(0f, -1f, 0f)
        }
        // zmax face
        else if(point.z==xmax)
        {
            return Normal(0f, 0f, 1f)
        }
        // zmin face
        else if(point.z==xmin)
        {
            return Normal(0f,0f, -1f)
        }

        return Normal()
    }

    fun cube_point_to_uv(point: Point):Vec2D
    {

        //save values to save time =)

        val absx:Float= abs(point.x)
        val absy:Float= abs(point.y)
        val absz:Float= abs(point.z)


        var maxAxis:Float=0f
        var uc:Float=0f
        var vc:Float=0f

        //faces  YZ
        if(point.x>0 && (absx>= absy ) && (absx>= absz) )
        {
            maxAxis= absx
            uc=-point.z
            vc=point.y
        }

        if (point.x<0 && (absx>=absy) && (absx>=absz))
        {
            maxAxis=absx
            uc=point.z
            vc=point.y
        }

        //faces ZX
        if(point.y>0 && (absy>=absx) && (absy>=absz))
        {
            maxAxis=absy
            uc=point.x
            vc=-point.z
        }
        if(point.y<0 && (absy>=absx) && (absy>=absz))
        {
            maxAxis=absy
            uc=point.x
            vc=point.z
        }

        //faces XY
        if(point.z>=0 && (absz>=absx) && (absz>=absy))
        {
            maxAxis=absz
            uc=point.x
            vc=point.y
        }

        if(point.z<=0 && (absz>=absx) && (absz>=absy))
        {
            maxAxis=absz
            uc=-point.x
            vc=point.y
        }

        val u:Float= 0.5f*((uc / maxAxis) + 1f)
        val v:Float= 0.5f*((vc/maxAxis) +1f)

        return Vec2D(u=u, v=v)
    }



}
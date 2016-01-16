package com.example.glup.demo2short3d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.animation.Animation;
import org.rajawali3d.animation.Animation3D;
import org.rajawali3d.animation.EllipticalOrbitAnimation3D;
import org.rajawali3d.animation.RotateAnimation3D;
import org.rajawali3d.animation.RotateOnAxisAnimation;
import org.rajawali3d.lights.PointLight;
import org.rajawali3d.loader.Loader3DSMax;
import org.rajawali3d.loader.LoaderAWD;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.RajawaliRenderer;
import org.rajawali3d.terrain.SquareTerrain;
import org.rajawali3d.terrain.TerrainGenerator;

import java.io.File;

/**
 * Created by Glup on 6/01/16.
 */
public class Renderer extends RajawaliRenderer {
    private PointLight mLight;
    private Object3D mObjectGroup;
    private Animation3D mCameraAnim, mLightAnim;

    public Renderer(Context context) {
        super(context);
    }

    @Override
    protected void initScene() {
        mLight = new PointLight();
        mLight.setPosition(0, 0, 4);
        mLight.setPower(0);
        getCurrentScene().setBackgroundColor(1,1,1,0.8f);
        //getCurrentScene().addLight(mLight);
        getCurrentCamera().setZ(4);

        Texture texture = new Texture("textura_prueba", BitmapFactory.decodeFile("mnt/sdcard/GlupFiles/textura_prueba.jpg"));
        //mTextureManager.addTexture(texture);
        //LoaderOBJ objParser = new LoaderOBJ(mContext.getResources(),mTextureManager, R.raw.obj_short_obj);
        //LoaderOBJ objParser = new LoaderOBJ(this,new File("mnt/sdcard/GlupFiles","obj_short_obj"));
        //LoaderOBJ objParser = new LoaderOBJ(this,new File("mnt/sdcard/GlupFiles","short_sin_suavizado"));
        //LoaderAWD objParser = new LoaderAWD(this,new File("mnt/sdcard/GlupFiles","awd_arrows.awd"));
        Loader3DSMax objParser = new Loader3DSMax(this,new File("mnt/sdcard/GlupFiles","short.3ds"));
        try {
            objParser.parse();

            mObjectGroup = objParser.getParsedObject();
            mObjectGroup.getMaterial().setColorInfluence(0);
            mObjectGroup.getMaterial().getTextureList().clear();
            mObjectGroup.setScale(1.4);


            Log.e("NumHijos", mObjectGroup.getNumChildren() + " Color "+mObjectGroup.getMaterial().getColor());

            Material material = new Material();
            material.getTextureList().clear();
            material.setColorInfluence(0);
            material.addTexture(texture);
            /*
            Bitmap bitmap= BitmapFactory.decodeFile("mnt/sdcard/GlupFiles/textura_prueba.jpg");
            SquareTerrain.Parameters parameters= SquareTerrain.createParameters(bitmap);
            parameters.setDivisions(128);
            parameters.setTextureMult(16);

            parameters.setColorMapBitmap(bitmap);
            SquareTerrain mTerrain = TerrainGenerator.createSquareTerrainFromBitmap(parameters,true);
            mTerrain.setMaterial(material);
            */
            if (mObjectGroup.getNumChildren()==0){
                mObjectGroup.setMaterial(material);
                mObjectGroup.setDoubleSided(true);
            }else{
                mObjectGroup.getChildAt(0).getMaterial().getTextureList().clear();
                mObjectGroup.getChildAt(0).setMaterial(material);
                mObjectGroup.getChildAt(0).setDoubleSided(true);
            }
            getCurrentScene().addChild(mObjectGroup);
            //getCurrentScene().addChild(mTerrain);

            mCameraAnim = new RotateOnAxisAnimation(Vector3.Axis.X, 360);
            mCameraAnim.setDurationMilliseconds(8000);
            mCameraAnim.setRepeatMode(Animation.RepeatMode.INFINITE);
            mCameraAnim.setTransformable3D(mObjectGroup);

        } catch (ParsingException e) {
            e.printStackTrace();
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }

        /*mLightAnim = new EllipticalOrbitAnimation3D(new Vector3(),
                new Vector3(0, 10, 0), Vector3.getAxisVector(Vector3.Axis.Z), 0,
                360, EllipticalOrbitAnimation3D.OrbitDirection.CLOCKWISE);

        mLightAnim.setDurationMilliseconds(3000);
        mLightAnim.setRepeatMode(Animation.RepeatMode.INFINITE);
        mLightAnim.setTransformable3D(mLight);*/

        getCurrentScene().registerAnimation(mCameraAnim);
        //getCurrentScene().registerAnimation(mLightAnim);

        mCameraAnim.play();
        //mLightAnim.play();
    }


    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }
}

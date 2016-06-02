/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.effect;

import com.jme3.effect.Particle;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.influencers.ParticleInfluencer;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.FastMath;
import name.huliqing.fighter.data.EffectData;
import name.huliqing.fighter.loader.Loader;

/**
 *
 * @author huliqing
 */
public class ParticleEffect extends AbstractEffect {
    private String emitter;
    // 是否立即执行所有粒子发射
    private boolean emitAll;
    // 是否随机颜色
    private boolean randomColor;
    // 粒子是否使用世界坐标位置，默认true,与粒子的默认设置相同
    private boolean inWorldSpace = true;
    // 粒子材质的混合模式，默认AlphaAdditive,这意味着粒子图片中的黑色背
    // 景会被过滤掉。这会导致无法创建黑色烟雾的效果，这个时候可以使用
    // Alpha模式来代替。
    private BlendMode blendMode = BlendMode.AlphaAdditive;
    
    // -- 内部
    private ParticleEmitter pe;

    @Override
    public void initData(EffectData data) {
        super.initData(data); 
        emitter = data.getAttribute("emitter", emitter);
        emitAll = data.getAsBoolean("emitAll", emitAll);
        randomColor = data.getAsBoolean("randomColor", randomColor);
        inWorldSpace = data.getAsBoolean("inWorldSpace", inWorldSpace);
        String tempBlendMode = data.getAttribute("blendMode");
        if (tempBlendMode != null) {
            blendMode = BlendMode.valueOf(tempBlendMode);
        }
    }

    @Override
    protected void doInit() {
        super.doInit();
        
        if (pe == null) {
            if (randomColor) {
                pe = Loader.loadEmitter(emitter, new RandomColorEmitter());
            } else {
                pe = Loader.loadEmitter(emitter);
            }
            pe.setInWorldSpace(inWorldSpace);
            pe.getMaterial().getAdditionalRenderState().setBlendMode(blendMode);
            localRoot.attachChild(pe);
        }
        if (emitAll) {
            pe.emitAllParticles();
        }
    }


    @Override
    public void cleanup() {
        if (pe != null) {
            pe.killAllParticles();
        }
        super.cleanup();
    }
    
    //------------- get and set
    
    public String getEmitter() {
        return emitter;
    }

    public void setEmitter(String emitter) {
        this.emitter = emitter;
        pe = null; // 需要重建
    }
    
    // 主要是为了随机颜色。
    private class RandomColorEmitter extends ParticleEmitter {
        
        public RandomColorEmitter() {
            super("MyEmitter", ParticleMesh.Type.Triangle, 0);
        }
        
        @Override
        public void setParticleInfluencer(ParticleInfluencer particleInfluencer) {
            super.setParticleInfluencer(particleInfluencer); 
        }

        @Override
        public void emitAllParticles() {
            super.emitAllParticles();
            changeColor();
        }

        @Override
        public void updateFromControl(float tpf) {
            super.updateFromControl(tpf);
            changeColor();
        }
        
        private void changeColor() {
            if (this.isEnabled()) {
                Particle[] particles = this.getParticles();
                if (particles != null && particles.length > 0) {
                    for (Particle p : particles) {
                        if (p.life <= 0) { 
                            continue;
                        }
                        p.color.set(FastMath.nextRandomFloat(), FastMath.nextRandomFloat(), FastMath.nextRandomFloat(), 1);
                    }
                }                
            }
        }
    }
}

 package inf112.skeleton.model.states.enemies;

 import com.badlogic.gdx.math.Vector2;
 import inf112.skeleton.model.entities.enemies.Enemy;
 import inf112.skeleton.view.ViewableEntity;

 import java.util.Random;

 public class PatrolState extends EnemyState{
     static Random rand = new Random();
     public static float vision = 32*8;

     private float idleTimer = 3;
     private float roamingTimer;
     private Vector2 direction = new Vector2();

     public PatrolState(Enemy enemy, ViewableEntity player){
         super(enemy, player);
     }

     @Override
     public void update(float deltaTime) {
         Vector2 playerCenter = player.getCenterPos();
         if (playerCenter.dst(enemy.getCenterPos()) <= vision) {
             enemy.changeState(new PursueState(enemy, player));
             return;
         }
         if(roamingTimer > 0){
             enemy.getVelocity().set(direction);
             enemy.moveEnemy(deltaTime);
             roamingTimer -= deltaTime;
             return;
         }
         if(idleTimer <= 0){
             idleTimer = rand.nextFloat(1.5f, 4);
             roamingTimer = rand.nextFloat(0.3f,0.6f);
             direction.set(1, 1);
             direction.rotateDeg(rand.nextInt(360));
         }
         idleTimer -= deltaTime;
     }

     @Override
     public String toString(){
         return "Patrol";
     }
 }

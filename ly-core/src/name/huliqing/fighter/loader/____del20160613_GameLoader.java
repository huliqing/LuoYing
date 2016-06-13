///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.loader;
//
//import name.huliqing.fighter.data.GameData;
//import name.huliqing.fighter.object.game.Game;
//import name.huliqing.fighter.object.game.StoryGbGame;
//import name.huliqing.fighter.object.game.StoryGuardGame;
//import name.huliqing.fighter.object.game.StoryTreasureGame;
//import name.huliqing.fighter.object.game.SurvivalGame;
//
///**
// *
// * @author huliqing
// */
//class GameLoader {
//
//    public static Game load(GameData data) {
//        String tagName = data.getProto().getTagName();
//        if (tagName.equals("gameStoryTreasure")) {
//            return new StoryTreasureGame(data);
//        } else if (tagName.equals("gameStoryGb")) {
//            return new StoryGbGame(data);
//        } else if (tagName.equals("gameStoryGuard")) {
//            return new StoryGuardGame(data);
//        } else if (tagName.equals("gameSurvival")) {
//            return new SurvivalGame(data);
//        }
//        throw new UnsupportedOperationException();
//    }
//}

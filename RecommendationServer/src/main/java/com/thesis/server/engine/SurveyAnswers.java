package com.thesis.server.engine;

import com.thesis.server.engine.parameters.StarRating;
import com.thesis.server.engine.parameters.Transfer;

public class SurveyAnswers {
    private StarRating starRating;
    private Transfer transfer;

    public StarRating getStarRating() {
        return starRating;
    }

    public Transfer getTransfer() {
        return transfer;
    }


//    private  List<String> transferOptions;
//    private  List<String> accommodationOptions;
//    private  List<String> wineAndDineOptions;
//    private  List<String> waterSportsOptions;
//    private  List<String> fitnessOptions;
//    private List<String> cateringOptions;
//    private  Integer transferPrice;
//    private  Integer transferTime;
//    private  Integer accommodationPrice;

//    private SurveyAnswers(SurveyAnswersBuilder builder) {
//        this.starRating = builder.starRating;
//        this.transferOptions = builder.transferOptions;
//        this.accommodationOptions = builder.accommodationOptions;
//        this.wineAndDineOptions = builder.wineAndDineOptions;
//        this.waterSportsOptions = builder.waterSportsOptions;
//        this.fitnessOptions = builder.fitnessOptions;
//        this.cateringOptions = builder.cateringOptions;
//        this.transferPrice = builder.transferPrice;
//        this.transferTime = builder.transferTime;
//        this.accommodationPrice = builder.accommodationPrice;
//    }
//
//    public static class SurveyAnswersBuilder {
//        private List<String> starRating;
//        private List<String> transferOptions;
//        private List<String> accommodationOptions;
//        private List<String> wineAndDineOptions;
//        private List<String> waterSportsOptions;
//        private List<String> fitnessOptions;
//        private List<String> cateringOptions;
//        private Integer transferPrice;
//        private Integer transferTime;
//        private Integer accommodationPrice;
//
//        public SurveyAnswersBuilder starRating(List<String> starRating){
//            this.starRating = starRating;
//            return this;
//        }
//        public SurveyAnswersBuilder transferOptions(List<String> transferOptions){
//            this.transferOptions = transferOptions;
//            return this;
//        }
//
//        public SurveyAnswersBuilder accommodationOptions(List<String> accommodationOptions){
//            this.accommodationOptions = accommodationOptions;
//            return this;
//        }
//
//        public SurveyAnswersBuilder wineAndDineOptions(List<String> wineAndDineOptions){
//            this.wineAndDineOptions = wineAndDineOptions;
//            return this;
//        }
//
//        public SurveyAnswersBuilder waterSportsOptions(List<String> waterSportsOptions){
//            this.waterSportsOptions = waterSportsOptions;
//            return this;
//        }
//
//        public SurveyAnswersBuilder fitnessOptions(List<String> fitnessOptions){
//            this.fitnessOptions = fitnessOptions;
//            return this;
//        }
//
//        public SurveyAnswersBuilder cateringOptions(List<String> cateringOptions){
//            this.cateringOptions = cateringOptions;
//            return this;
//        }
//
//        public SurveyAnswersBuilder transferPrice(Integer transferPrice){
//            this.transferPrice = transferPrice;
//            return this;
//        }
//
//        public SurveyAnswersBuilder transferTime(Integer transferTime){
//            this.transferTime = transferTime;
//            return this;
//        }
//
//        public SurveyAnswersBuilder accommodationPrice(Integer accommodationPrice){
//            this.accommodationPrice = accommodationPrice;
//            return this;
//        }
//
//        public SurveyAnswers build() {
//            SurveyAnswers answers = new SurveyAnswers(this);
//            return answers;
//        }
//
//    }
}

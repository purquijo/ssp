package purquijo.games.game.entity;

public enum ChoiceType {
    STONE {
        @Override
        public ChoiceResult against(ChoiceType choiceType) {
            return switch (choiceType) {
                case STONE -> ChoiceResult.DRAW;
                case SCISSORS -> ChoiceResult.WIN;
                case PAPER -> ChoiceResult.LOSE;
            };
        }
    },
    SCISSORS {
        @Override
        public ChoiceResult against(ChoiceType choiceType) {
            return switch (choiceType) {
                case STONE -> ChoiceResult.LOSE;
                case SCISSORS -> ChoiceResult.DRAW;
                case PAPER -> ChoiceResult.WIN;
            };
        }
    },
    PAPER {
        @Override
        public ChoiceResult against(ChoiceType choiceType) {
            return switch (choiceType) {
                case STONE -> ChoiceResult.WIN;
                case SCISSORS -> ChoiceResult.LOSE;
                case PAPER -> ChoiceResult.DRAW;
            };
        }
    };

    public abstract ChoiceResult against (ChoiceType choiceType);

}

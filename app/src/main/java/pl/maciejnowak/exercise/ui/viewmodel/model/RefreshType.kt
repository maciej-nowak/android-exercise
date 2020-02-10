package pl.maciejnowak.exercise.ui.viewmodel.model

enum class RefreshType(val value: Boolean) {
    NORMAL(false), FORCE(true);

    companion object {
        fun valueOf(value: Boolean): RefreshType {
            for (type in values()) {
                if (type.value == value) {
                    return type
                }
            }
            return NORMAL
        }
    }
}
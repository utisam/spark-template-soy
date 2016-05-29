package com.station.soy;

import com.google.template.soy.tofu.SoyTofu.Renderer;

import spark.ResponseTransformer;

public class SoyTemplateEngine implements ResponseTransformer {

	@Override
	public String render(Object model) throws Exception {
		if (model == null) return null;
		if (!(model instanceof Renderer)) {
			throw new IllegalArgumentException();
		}
		Renderer renderer = (Renderer) model;

		return renderer.render();
	}

}
